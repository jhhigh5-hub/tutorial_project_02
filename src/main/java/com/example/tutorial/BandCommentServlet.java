package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Band;
import com.example.tutorial.vo.Comment;
import com.example.tutorial.vo.Member;
import com.example.tutorial.vo.Posts;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;
@WebServlet("/band/comment")
public class BandCommentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int no = Integer.parseInt(req.getParameter("no"));
        int bandNo;
        bandNo = Integer.parseInt(req.getParameter("bandNo"));

        try (SqlSession sqlSession = MybatisUtil.build().openSession(true)) {
            Posts post = sqlSession.selectOne("mappers.PostsMapper.selectOneByNo", no);
            List<Comment> comments = sqlSession.selectList("mappers.PostsMapper.selectCommentsByPostNo", no);

            req.setAttribute("post", post);
            req.setAttribute("comments", comments);
            req.setAttribute("band", bandNo);
            req.getRequestDispatcher("/band/board?no=" + bandNo).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1️⃣ 로그인 사용자 확인
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        if (logonUser == null) {
            resp.sendRedirect("/member/login");
            return;
        }
        req.setAttribute("auth", logonUser != null);

        // 2️⃣ 파라미터 가져오기
        String postNoStr = req.getParameter("postNo");  // 먼저 문자열로 받음
        String content = req.getParameter("content");

        // 3️⃣ 내용 검사
        if (postNoStr == null || postNoStr.isEmpty() || content == null || content.trim().isEmpty()) {
            resp.sendRedirect("/band/board/detail?no=" + postNoStr + "&error=emptyComment");
            return;
        }

        int postNo;
        int bandNo;

        bandNo = Integer.parseInt(req.getParameter("bandNo"));
        try {
            postNo = Integer.parseInt(postNoStr);  // 문자열을 int로 변환
        } catch (NumberFormatException e) {
            resp.sendRedirect("/band/board?no=" + bandNo);
            return;
        }


        // 4️⃣ Comment 객체 생성
        Comment comment = new Comment();
        comment.setPostNo(postNo);
        comment.setWriterId(logonUser.getId());
        comment.setContent(content);


        // 5️⃣ MyBatis로 DB 저장
        try (SqlSession sqlSession = MybatisUtil.build().openSession(true)) {
            sqlSession.insert("mappers.PostsMapper.insertComment", comment);
        }

        // 6️⃣ 다시 게시글 상세 페이지로 이동

        resp.sendRedirect("/band/board?no=" + bandNo);

    }
}
