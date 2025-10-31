package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Band;
import com.example.tutorial.vo.Member;
import com.example.tutorial.vo.Posts;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/band/board")
public class BandBoardServlet extends HttpServlet {
    private static final int POSTS_PER_PAGE = 10;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 로그인 유저 정보 가져오기
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        req.setAttribute("auth", logonUser != null);

        // 1. bandNo 파라미터 가져오기
        int bandNo;
        try {
            // URL 파라미터 이름을 "bandNo"로 통일하는 게 좋아. (네가 쓴 "/band?no=" 이라면 "no"로 받아야 함)
            bandNo = Integer.parseInt(req.getParameter("no")); // <--- generatedBandNo가 여기에 실려옴!
        } catch (NumberFormatException e) {
            System.out.println(e);
            return;
        }

        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtil.build().openSession(); // SELECT 작업이므로 auto commit = false

            // bandNo로 Band 정보를 가져옴
            Band band = sqlSession.selectOne("mappers.BandMapper.selectByNo", bandNo);
            if (band == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "밴드를 찾을 수 없습니다.");
                return;
            }
            req.setAttribute("band", band); // 밴드 정보 JSP에 전달 (밴드명, 멤버수 등)

            // (옵션) 밴드 멤버십 상태 확인 등 추가 로직
            // req.setAttribute("isBandMember", isMember);

            // 게시글 목록 및 페이지네이션 처리
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
            // 총 게시글 수 조회
            int totalPostsCount = sqlSession.selectOne("mappers.PostsMapper.countAllByBandNo", bandNo);

            int lastPage = totalPostsCount / POSTS_PER_PAGE;
            if (totalPostsCount % POSTS_PER_PAGE > 0) {
                lastPage++;
            }
            if (page < 1) page = 1;
            if (page > lastPage && lastPage > 0) page = lastPage;

            int offset = (page -1) * POSTS_PER_PAGE;

            Map<String, Object> postsParam = new HashMap<>();
            postsParam.put("bandNo", bandNo);
            postsParam.put("offset", offset);
            postsParam.put("amount", POSTS_PER_PAGE);

            List<Posts> postsList = sqlSession.selectList("mappers.PostsMapper.selectAllByBanNo", postsParam);

            req.setAttribute("postsList", postsList);
            req.setAttribute("totalPostsCount", totalPostsCount);
            req.setAttribute("lastPage", lastPage);
            req.setAttribute("currentPage", page);

        } catch (Exception e) {
            System.out.println(e);
            req.setAttribute("errorMessage", "밴드 정보를 불러오는 중 오류가 발생했습니다.");
            return;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
        req.getRequestDispatcher("/band/board.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        String hashtag = req.getParameter("hashtag");
        String content = req.getParameter("content");

        if (content == null || content.trim().isEmpty()) {
            req.setAttribute("error", "내용은 필수입니다.");
            req.getParameter("no");
            return;
        }

        Posts posts = new Posts();
        posts.setWriterId(logonUser.getId());
        posts.setHashtag(hashtag);
        posts.setContent(content);

        Band band = new Band();

        SqlSession sqlSession = MybatisUtil.build().openSession(true);
        int r = sqlSession.insert("mappers.BandMapper.insertOne", posts);

        sqlSession.close();

        resp.sendRedirect("/band/board?no=" + band.getNo());
    }

}