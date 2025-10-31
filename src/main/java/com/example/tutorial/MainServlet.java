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
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        if(logonUser == null){
            resp.sendRedirect("/member/login");
            return;
        }
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtil.build().openSession();
            List<Band> myJoinedBands = sqlSession.selectList("mappers.BandMapper.selectBandList", logonUser.getId());
            req.setAttribute("myJoinedBands", myJoinedBands);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            sqlSession.close();
        }
        req.getRequestDispatcher("/main.jsp").forward(req,resp);
    }
}
