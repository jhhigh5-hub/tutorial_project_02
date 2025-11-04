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
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/band/posts-all")
public class AllPostsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        if (logonUser == null) {
            resp.sendRedirect("/member/login");
        }

        String writerId = logonUser.getId();

        SqlSession sqlSession = MybatisUtil.build().openSession(true);

        List<Posts> myPosts = sqlSession.selectList("mappers.PostsMapper.selectMyAllPosts", writerId);
        req.setAttribute("myPosts", myPosts);


        req.getRequestDispatcher("/band/posts-all.jsp").forward(req, resp);
    }

}
