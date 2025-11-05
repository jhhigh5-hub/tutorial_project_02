package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.*;
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
        if (logonUser == null) {
            resp.sendRedirect("/member/login");
        }
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


            // userBandStatus 로직 추가
            String userBandStatus = "GUEST";
            if (logonUser.getId().equals(band.getCreateMaster())) {
                userBandStatus = "MASTER";
            } else {
                Map<String, Object> params = new HashMap<>();
                params.put("bandNo", bandNo);
                params.put("memberId", logonUser.getId());
                BandJoinRequest existingRequest = sqlSession.selectOne("mappers.BandJoinRequestMapper.selectBandJoinStatus", params);

                if (existingRequest != null) {
                    if ("pending".equals(existingRequest.getJoinStatus())) {
                        userBandStatus = "PENDING"; // 가입 신청 대기중
                    } else if ("approved".equals(existingRequest.getJoinStatus())) {
                        userBandStatus = "JOINED"; // 승인 완료
                    } else if ("rejected".equals(existingRequest.getJoinStatus())) {
                        // 거절 상태면 재신청 가능하게 'NONE_JOINED'로 처리할 수도 있고,
                        // 'REJECTED' 상태로 jsp에 표시해서 재신청 불가하게 할 수도 있음.
                        // 여기서는 재신청 가능하도록 'NONE_JOINED'로 처리
                        userBandStatus = "NONE_JOINED";
                    }
                } else {
                    // 3. band_member 테이블에 직접 가입되어 있는지 확인 (가입 요청 없이 바로 가입된 경우)
                    // 이 부분은 band_member테이블이 잇다면 추가해야함.
                    userBandStatus = "NONE_JOINED";
                }
            }
            req.setAttribute("userBandStatus", userBandStatus);

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

            int offset = (page - 1) * POSTS_PER_PAGE;

            Map<String, Object> postsParam = new HashMap<>();
            postsParam.put("bandNo", bandNo);
            postsParam.put("offset", offset);
            postsParam.put("amount", POSTS_PER_PAGE);

            List<Posts> postsList = sqlSession.selectList("mappers.PostsMapper.selectAllByBanNo", postsParam);

            // 2️⃣ 각 게시글에 댓글 목록 추가
            for (Posts post : postsList) {
                List<Comment> comments = sqlSession.selectList(
                        "mappers.PostsMapper.selectCommentsByPostNo", post.getNo()
                );
                post.setComments(comments);

                // 좋아요 여부 확인
                if (logonUser != null) {
                    Map<String, Object> likeParam = new HashMap<>();
                    likeParam.put("memberId", logonUser.getId());
                    likeParam.put("postNo", post.getNo());
                    Integer count = sqlSession.selectOne("mappers.PostsLikeMapper.exists", likeParam);
                    post.setAlreadyLike(count != null && count > 0);
                } else {
                    post.setAlreadyLike(false);
                }
            }

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
        int bandNo = Integer.parseInt(req.getParameter("bandNo"));
        String hashtag = req.getParameter("hashtag");
        String content = req.getParameter("content");

        if (content == null || content.trim().isEmpty()) {
            req.setAttribute("error", "내용은 필수입니다.");
            req.getParameter("no");
            return;
        }

            SqlSession sqlSession = MybatisUtil.build().openSession(true);

            // ✅ 1️⃣ 이 사용자가 밴드에 가입된 상태인지 확인
            Map<String, Object> params = new HashMap<>();
            params.put("bandNo", bandNo);
            params.put("memberId", logonUser.getId());

            BandJoinRequest joinStatus = sqlSession.selectOne(
                    "mappers.BandJoinRequestMapper.selectBandJoinStatus",
                    params
            );

            boolean isJoined = false;
            if (joinStatus != null && "approved".equals(joinStatus.getJoinStatus())) {
                isJoined = true;
            }

            // ✅ 2️⃣ 가입되지 않은 유저는 글쓰기 금지
            if (!isJoined) {
                req.setAttribute("errorMessage", "밴드에 가입된 멤버만 게시글을 작성할 수 있습니다.");
                return;
            }

            Posts posts = new Posts();
            posts.setWriterId(logonUser.getId());
            posts.setHashtag(hashtag);
            posts.setContent(content);
            posts.setBandNo(bandNo);

            int r = sqlSession.insert("mappers.PostsMapper.insertOne", posts);

            sqlSession.close();

            resp.sendRedirect("/band/board?no=" + bandNo);
        }
}