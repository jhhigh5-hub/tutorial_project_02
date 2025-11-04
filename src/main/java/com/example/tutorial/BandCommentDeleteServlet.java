package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Comment;
import com.example.tutorial.vo.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

@WebServlet("")
public class BandCommentDeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 로그인 유저 정보 가져오기 (댓글 삭제도 로그인 필요하겠지?)
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        if(logonUser == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
            return;
        }

        // 2. 삭제할 댓글 번호, 리다이렉트할 게시글 번호 파라미터로 받아오기
        int commentNo;
        int postNoForRedirect; // 댓글 삭제 후 원래 게시글 상세 페이지로 돌아갈 때 필요
        try {
            commentNo = Integer.parseInt(req.getParameter("commentNo"));
            postNoForRedirect = Integer.parseInt(req.getParameter("postNo")); // 댓글이 속한 게시글 번호
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다. (댓글 번호 또는 게시글 번호가 올바르지 않음)");
            return;
        }

        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtil.build().openSession(false); // auto-commit 끄고 시작

            // 3. 댓글 정보 조회 (누가 쓴 댓글인지 확인하려고)
            Comment comment = sqlSession.selectOne("mappers.PostsMapper.selectOneByNo", commentNo);

            if(comment == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "삭제할 댓글을 찾을 수 없습니다.");
                return;
            }

            // 4. 권한 확인: 본인 댓글인지, 또는 게시글 작성자나 관리자 권한인지 등
            // 여기서는 일단 댓글 작성자 본인인지 확인
            if (!comment.getWriterId().equals(logonUser.getId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "본인 댓글만 삭제할 수 있습니다.");
                return;
            }

            // 5. 댓글 삭제 (PostsMapper에 있다면 CommentsMapper 같은걸 새로 만들어야 할 수도)
            sqlSession.delete("mappers.PostsMapper.deleteCommentsByPostNo", commentNo); // 댓글 하나만 샥~

            sqlSession.commit(); // 다 성공했으면 커밋!

            // 6. 삭제 완료 후 해당 게시글 상세 페이지로 리다이렉트
            resp.sendRedirect("/band/post/detail?no=" + postNoForRedirect); // 게시글 상세 페이지로 이동하도록

        } catch (Exception e) {
            if(sqlSession != null) sqlSession.rollback(); // 에러나면 롤백
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "댓글 삭제 중 오류가 발생했습니다.");
        } finally {
            if(sqlSession != null) sqlSession.close();
        }

    }
}
