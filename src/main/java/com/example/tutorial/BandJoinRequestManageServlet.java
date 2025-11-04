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
        PrintWriter out = resp.getWriter();
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");

        if (logonUser == null) {
            resp.sendRedirect("/member/login");
            return;
        }

        int bandNo = Integer.parseInt(req.getParameter("bandNo"));
        String action = req.getParameter("action"); // approved 또는 rejected
        String memberId = req.getParameter("memberId");

        String newStatus = "pending";
        if ("approve".equals(action)) {
            newStatus = "approved";
        } else if ("reject".equals(action)) {
            newStatus = "rejected";
        }

        SqlSession sqlSession = MybatisUtil.build().openSession(true);
        Map<String, Object> checkParams = new HashMap<>();
        checkParams.put("bandNo", bandNo);
        checkParams.put("memberId", memberId);
        checkParams.put("joinStatus", newStatus);

        int r = sqlSession.update("mappers.BandJoinRequestMapper.updateBandJoinRequestStatus", checkParams);
        System.out.println("r: " + r);

        if (r > 0 && "approve".equals(action)) {
            int updateMemberCnt = sqlSession.update("mappers.BandJoinRequestMapper.increaseBandMemberCnt", bandNo);
            System.out.println("doPost: bandNo " + bandNo + "memberCnt result: " + updateMemberCnt);
//            if (updateMemberCnt > 0) {
//                out.println("SUCCESS: 가입 승인, 멤버 수 업데이트 완료");
//            } else {
//                out.println("FAIL: 가입 승인, 멤버 수 업데이트 실패");
//            }
//        } else if (r > 0 && action.equals("reject")) {
//            out.println("SUCCESS: 가입 신청이 거절되었습니다.");
//        } else {
//            out.print("FAIL: 요청처리 중 문제가 발생했습니다. 이미 처리된 요청이거나 잘못된 요청입니다.");
        }

        sqlSession.close();

        resp.sendRedirect("/band/join-request-manage?no=" + bandNo);
    }
}
