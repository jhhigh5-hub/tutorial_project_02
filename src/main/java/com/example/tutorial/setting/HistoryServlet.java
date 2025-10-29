package com.example.tutorial.setting;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.LoginHistory;
import com.example.tutorial.vo.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/setting/history")
public class HistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getSession().getAttribute("logonUser") == null) {
            resp.sendRedirect("/member/login");
            return;
        }

        Member logonUser = (Member) req.getSession().getAttribute("logonUser");

        SqlSession sqlSession = MybatisUtil.build().openSession(true);

        List<LoginHistory> list = sqlSession.selectList("mappers.LoginHistoryMapper.selectByMemberId", logonUser.getId());

        req.setAttribute("list", list);
        req.setAttribute("auth", true);
        req.getRequestDispatcher("/setting/history.jsp").forward(req, resp);
    }
}
