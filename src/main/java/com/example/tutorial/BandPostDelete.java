package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Member;
import com.example.tutorial.vo.Posts;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

@WebServlet("/band/post/delete")
public class BandPostDelete extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 로그인 유저 정보 가져오기
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        req.setAttribute("auth", logonUser != null);


        int postNo;
        int bandNo;

        try {
            postNo = Integer.parseInt(req.getParameter("postNo"));
            bandNo = Integer.parseInt(req.getParameter("bandNo"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
            return;
        }

        SqlSession sqlSession = null;

        try {
            sqlSession = MybatisUtil.build().openSession(true);

            // 2. 게시글 존재 여부 확인
            Posts post = sqlSession.selectOne("mappers.PostsMapper.selectOneByNo", postNo);

            if (post == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "게시글을 찾을 수 없습니다.");
                return;
            }

            // 3. 작성자 본인 확인
            if (!post.getWriterId().equals(logonUser.getId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "본인 게시글만 삭제할 수 있습니다.");
                return;
            }

            // 4. 댓글 먼저 삭제
            sqlSession.delete("mappers.PostsMapper.deleteCommentsByPostNo", postNo);

            // 5. 게시글 삭제
            sqlSession.delete("mappers.PostsMapper.deletePostByNo", postNo);

            // 6. 삭제 완료 후 밴드 게시판으로 리다이렉트
            resp.sendRedirect("/band/board?no=" + bandNo);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "게시글 삭제 중 오류가 발생했습니다.");
        } finally {
            if (sqlSession != null) sqlSession.close();
        }
    }

}
