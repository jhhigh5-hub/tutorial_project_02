package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Member;
import com.example.tutorial.vo.Posts;
import com.example.tutorial.vo.PostsLike;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/posts/like")
public class PostsLikeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {

        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        if (logonUser == null) {
            resp.sendRedirect("/login");
            return;
        }

        int postNo = Integer.parseInt(req.getParameter("postNo"));

        PostsLike like = new PostsLike();
        like.setMemberId(logonUser.getId());
        like.setPostNo(postNo);

        try (SqlSession sqlSession = MybatisUtil.build().openSession(true)) {
            Integer exists = sqlSession.selectOne("mappers.PostsLikeMapper.exists", like);

            if (exists != null && exists > 0) {
                sqlSession.delete("mappers.PostsLikeMapper.deleteByMemberIdAndArticleNo", like);
            } else {
                sqlSession.insert("mappers.PostsLikeMapper.insertOne", like);
            }
        }

        // 게시판 페이지로 다시 이동
        resp.sendRedirect("/band/board?no=" + req.getParameter("bandNo"));
    }

}
