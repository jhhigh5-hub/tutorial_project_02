package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.BandJoinRequest;
import com.example.tutorial.vo.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

@WebServlet("/band/join-request")
public class BandJoinRequestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        int bandNo = Integer.parseInt(req.getParameter("bandNo"));

        SqlSession sqlSession = MybatisUtil.build().openSession(true);
        BandJoinRequest joinReq = new BandJoinRequest();
        joinReq.setMemberId(logonUser.getId());
        joinReq.setBandNo(bandNo);
        int r = sqlSession.insert("mappers.BandJoinRequestMapper.insertBandJoinRequest", joinReq);

        resp.sendRedirect("/band/board?no=" + bandNo);
    }
}
