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
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/band/join-admin")
public class BandMemberAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            sqlSession = MybatisUtil.build().openSession();
            Band band = sqlSession.selectOne("mappers.BandMapper.selectBandByNo", bandNo);
            if (band == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "밴드를 찾을 수 없습니다.");
                return;
            }

            List<BandJoinRequest> pendingRequests = sqlSession.selectList("mappers.BandJoinRequestMapper.selectPendingJoinRequestsByBandNo", bandNo);

            req.setAttribute("band", band);
            req.setAttribute("pendingRequests", pendingRequests);
            req.setAttribute("logonUser", logonUser);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            sqlSession.close();
        }
        req.getRequestDispatcher("/band/joinRequestList.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        if (logonUser == null) {
            resp.sendRedirect("/member/login");
            return;
        }

        int bandNo;
        int requestIdx;
        String action;

        try {
            bandNo = Integer.parseInt(req.getParameter("bandNo"));
            requestIdx = Integer.parseInt(req.getParameter("requestIdx"));
            action = req.getParameter("action");
            if (!("approve".equals(action) || "reject".equals(action))) {
                throw new IllegalArgumentException("유효하지 않은 처리 액션입니다.");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 파라미터입니다.");
            return;
        }

        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtil.build().openSession(true);
            Band band = sqlSession.selectOne("mappers.BandMapper.selectBandByNo", bandNo);
            if (band == null || !band.getCreateMaster().equals(logonUser.getId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "밴드 마스터만 가입 신청을 처리할 수 있습니다.");
                return;
            }

            Map<String, Object> param = Map.of(
                    "idx", requestIdx,
                    "bandNo", bandNo,
                    "joinStatus", ("approve".equals(action) ? "approved" : "rejected")
            );
            int r = sqlSession.update("mappers.BandJoinMapper.updateBandJoinRequestStatus", param);
            if (r == 1 && "approve".equals(action)) {
                sqlSession.update("mappers.BandJoinMapper.increaseBandMemberCnt", bandNo);
            }
            resp.sendRedirect("/band/");

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            sqlSession.close();
        }
    }
}

