package com.example.tutorial;


import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

@WebServlet("/member/signup")
public class MemberSignUpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/member/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String pw = req.getParameter("pw");
        String email = req.getParameter("email");
        String name = req.getParameter("name");
        String nickname = req.getParameter("nickname");

        req.setAttribute("id", id);
        req.setAttribute("pw", pw);
        req.setAttribute("email", email);
        req.setAttribute("name", name);
        req.setAttribute("nickname", nickname);
        boolean isValid = true;

        if (!id.matches("[a-zA-Z0-9]{3,15}")) {
            req.setAttribute("idFormatERR", "아이디는 4~15자리이거나, 특수문자를 사용할 수 없습니다.");
            isValid = false;
        }

        if (!pw.matches("(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,}")) {
            req.setAttribute("pwFormatERR", "비밀번호는 영문자와 숫자를 포함하여 6자리 이상이어야 합니다.");
            isValid = false;
        }

        if (!email.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")) {
            req.setAttribute("emailFormatERR", "유효하지 않은 이메일 형식입니다.");
            isValid = false;
        }

        if (!isValid) {
            req.getRequestDispatcher("/member/signup-fail.jsp").forward(req, resp);
            return;
        }

        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtil.build().openSession(true);

            Member foundId = sqlSession.selectOne("mappers.MemberMapper.selectById", id);
            Member foundNickname = sqlSession.selectOne("mappers.MemberMapper.selectByNickname", nickname);

            boolean isDuplicated = false;
            if (foundId != null) {
                req.setAttribute("idERR", "이미 사용중인 아이디 입니다.");
                isDuplicated = true;
            }
            if (foundNickname != null) {
                req.setAttribute("nicknameERR", "이미 사용중인 닉네임 입니다.");
                isDuplicated = true;
            }

            if (isDuplicated) {
                req.getRequestDispatcher("/member/signup-fail.jsp").forward(req, resp);
                return;
            }

            Member member = new Member();
            member.setId(id);
            member.setPw(pw);
            member.setEmail(email);
            member.setName(name);
            member.setNickname(nickname);

            int r = sqlSession.insert("mappers.MemberMapper.insertOne", member);

            if (r == 1) {
                req.getRequestDispatcher("/main.jsp").forward(req, resp);
            } else {
                req.setAttribute("generalERR", "회원가입에 실패했습니다. 다시 시도해주세요.");
                req.getRequestDispatcher("/member/signup-fail.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            System.out.println(e);
            req.setAttribute("generalERR", "서버 오류로 인해 회원가입에 실패했습니다.");
            req.getRequestDispatcher("/member/signup-fail.jsp").forward(req, resp);
        } finally {

            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
