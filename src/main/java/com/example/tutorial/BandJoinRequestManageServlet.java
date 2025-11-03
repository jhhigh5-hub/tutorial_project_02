package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Band;
import com.example.tutorial.vo.BandJoinRequest;
import com.example.tutorial.vo.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet("/band/join-request-manage")
public class BandJoinRequestManageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        if (logonUser == null) {
            resp.sendRedirect("/member/login");
            return;
        }

        int bandNo = Integer.parseInt(req.getParameter("no"));

        System.out.println("bandNo 파라미터: " + req.getParameter("no"));
        System.out.println("로그인한 유저: " + logonUser.getId());

        SqlSession sqlSession = MybatisUtil.build().openSession(true);

        Band band = sqlSession.selectOne("mappers.BandMapper.selectByNo", bandNo);
        if (band == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "밴드를 찾을 수 없습니다.");
            return;
        }
        req.setAttribute("band", band);

        List<BandJoinRequest> pendingRequests = sqlSession.selectList("mappers.BandJoinRequestMapper.selectPendingJoinRequestByBandNo", bandNo);
        req.setAttribute("pendingRequests", pendingRequests);
        sqlSession.close();

//        resp.sendRedirect("/band/join-request-manage?no=" + bandNo);
        req.getRequestDispatcher("/band/approve-request.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        Map<String, Object> responseMap = new HashMap<>();

        if (logonUser == null) {
            resp.sendRedirect("/member/login");
            return;
        }

        int idx;
        int bandNo;
        String action = req.getParameter("action"); // approve 또는 reject

        try {
            idx = Integer.parseInt(req.getParameter("idx"));
            bandNo = Integer.parseInt(req.getParameter("bandNo"));
            if (action == null || (!action.equals("approve")) && !action.equals("reject")) {
                throw new IllegalArgumentException("유효하지 않은 처리 액션입니다.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청 파라미터입니다.");
            return;
        }

        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtil.build().openSession(true);
            Map<String, Object> checkParams = new HashMap<>();
            checkParams.put("bandNo", bandNo);
            checkParams.put("memberId", logonUser.getId());


        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "가입 요청 처리 중 오류 발생");
        } finally {
            sqlSession.close();
        }
    }
}
