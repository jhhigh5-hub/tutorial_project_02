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

        int bandNo;
        try {
            bandNo = Integer.parseInt(req.getParameter("bandNo"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 밴드 번호입니다.");
            return;
        }

        System.out.println("bandNo 파라미터: " + req.getParameter("bandNo"));
        System.out.println("로그인한 유저: " + logonUser.getId());
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtil.build().openSession(true);

            Band band = sqlSession.selectOne("mappers.BandMapper.selectByNo", bandNo);
            if (band == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "밴드를 찾을 수 없습니다.");
                return;
            }

            if (!logonUser.getId().equals(band.getCreateMaster())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "밴드 마스터만 접근할 수 있습니다.");
                return;
            }

            List<BandJoinRequest> pendingRequests = sqlSession.selectList("mappers.BandJoinRequestMapper.selectPendingJoinRequestByBandNo", bandNo);
            req.setAttribute("band", band);
            req.setAttribute("pendingRequests", pendingRequests);
            req.getRequestDispatcher("/band/approve-requests.jsp").forward(req, resp);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

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
            BandJoinRequest existingRequest = sqlSession.selectOne("mappers.BandJoinRequestMapper.selectJoinRequestStatus", checkParams);
            if (existingRequest != null) {
                String currentStatus = existingRequest.getJoinStatus();
                if ("pending".equals(currentStatus)) {
                    out.println("FAIL: 이미 가입 신청 대기중입니다.");
                    return;
                } else if ("approved".equals(currentStatus)) {
                    out.println("FAIL: 이미 이 밴드의 멤버입니다.");
                    return;
                }
            }

            BandJoinRequest bjr = new BandJoinRequest();
            bjr.setBandNo(bandNo);
            bjr.setMemberId(logonUser.getId());

            int r = sqlSession.insert("mappers.BandJoinRequestMapper.insertBandJoinRequest", bjr);
            if ( r > 0) {
                out.println("SUCCESS: 가입 신청이 완료되었습니다. 마스터의 승인을 기다려주세요.");
            } else {
                out.println("FAIL: 가입 신청에 실패했습니다. (DB 삽입 실패)");
            }
//            Band band = sqlSession.selectOne("mappers.BandMapper.selectByNo", bandNo);
//            if (band == null || !logonUser.getId().equals(band.getCreateMaster())) {
//                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "밴두 마스터만 처리할 수 있습니다.");
//                return;
//            }
//            Map<String, Object> updateParams = new HashMap<>();
//            updateParams.put("idx", idx);
//            updateParams.put("joinStatus", action.equals("approve") ? "approved" : "rejected");
//            int r = sqlSession.update("mappers.BandJoinRequestMapper.updateJoinStatus", updateParams);
//            if (r > 0 && action.equals("approve")) {
//                sqlSession.update("mappers.BandJoinRequestMapper.increaseBandMemberCnt", bandNo);
//            }
//            resp.sendRedirect(req.getContextPath() + "/band/approver-request?bandNo=" + bandNo + "&msg=request_processed");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "가입 요청 처리 중 오류 발생");
        } finally {
            sqlSession.close();
        }
    }
}
