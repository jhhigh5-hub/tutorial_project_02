package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Band;
import com.example.tutorial.vo.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member longonUser = (Member) req.getSession().getAttribute("longonUser");
        if(longonUser == null){
            resp.sendRedirect("/login");
            return;
        }
        SqlSession sqlSession = MybatisUtil.build().openSession(true);
        List<Band> band = sqlSession.selectList("mappers.BandListMapper.selectBandList", longonUser.getId());
        req.setAttribute("band", band);
        req.getRequestDispatcher("/main-top.jspf").forward(req,resp);
    }
}
