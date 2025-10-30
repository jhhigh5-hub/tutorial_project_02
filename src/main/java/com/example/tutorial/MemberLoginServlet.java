package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;


import java.io.IOException;

@WebServlet("/member/login")
public class MemberLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/member/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String pw = req.getParameter("pw");
        String keepLogin = req.getParameter("keepLogin");
        SqlSession sqlSession = MybatisUtil.build().openSession(true);

        Member found = sqlSession.selectOne("mappers.MemberMapper.selectById", id);

        if (found != null && found.getPw().equals(pw)) {
            //===============================================================================
            if (keepLogin != null) {
                Cookie cookie = new Cookie("keepLogin", id);
                cookie.setMaxAge(60 * 60 * 24 * 30);
                resp.addCookie(cookie);
            }
            //=============================================================================
            int r = sqlSession.insert("mappers.LoginHistoryMapper.insertOne", id);
            req.getSession().setAttribute("logonUser", found);

            resp.sendRedirect("/main");
        } else {
            req.setAttribute("tryId", id);
            req.getRequestDispatcher("/member/login-fail.jsp").forward(req, resp);
        }
        sqlSession.close();
    }
}
