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
import java.util.Map;

@WebServlet("/band/join")
public class BandJoinRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        if (logonUser == null) {
            resp.sendRedirect("/member/login");
            return;
        }

        int bandNo;
        try {
            bandNo = Integer.parseInt(req.getParameter("bandNo"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 밴드 번호입니다.");
            return;
        }
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtil.build().openSession(true);

            Map<String, Object> param = Map.of("bandNo", bandNo, "memberId", logonUser.getId());
            String existingStatus = sqlSession.selectOne("mappers.BandJoinMapper.selectBandJoinStatus", param);

            if (existingStatus != null) {
                String message;
                if ("pending".equals(existingStatus)) {
                    message = "already_requested";
                } else if ("approved".equals(existingStatus)) {
                    message = "already_member";
                } else {
                    message = "already_processed";
                }
                resp.sendRedirect("/band/board?no=" + bandNo + "$message=" + message);
                return;
            }

            BandJoinRequest bjr = new BandJoinRequest();
            bjr.setBandNo(bandNo);
            bjr.setMemberId(logonUser.getId());
            bjr.setJoinStatus("pending");

            int r = sqlSession.insert("mappers.BandJoinMapper.insertBandJoinRequest", bjr);
            if (r == 1) {
                resp.sendRedirect("/band/board?no=" + bandNo);
            } else {
                resp.sendRedirect("/band/board?no=" + bandNo);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            sqlSession.close();
        }
    }
}
