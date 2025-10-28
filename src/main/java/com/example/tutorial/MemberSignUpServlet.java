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

        Member member = new Member();
        member.setId(id);
        member.setPw(pw);
        member.setEmail(email);
        member.setName(name);
        member.setNickname(nickname);

        SqlSession sqlSession = MybatisUtil.build().openSession(true);
        int r = 0;
        Member foundId = sqlSession.selectOne("mappers.MemberMapper.selectById", id);
        Member foundNickname = sqlSession.selectOne("mappers.MemberMapper.selectByNickname", nickname);
        if (id.matches("[a-zA-Z0-9]{4,15}") && pw.matches("(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,}") && email.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            if (foundId == null && foundNickname == null) {
                r = sqlSession.insert("mappers.MemberMapper.insertOne", member);
            } else {
                if (!id.matches("[a-zA-Z0-9]{4,15}")) {
                    req.setAttribute("idFormatERR", "아이디는 특수문자를 사용할 수 없습니다.");
                }
                if (!pw.matches("(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,}")) {
                    req.setAttribute("pwFormatERR", "비밀번호는 영문자와 숫자를 포함하여 6자리 이상이어야 합니다.");
                }
                if (!email.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
                    req.setAttribute("emailFormatERR", "유효하지 않은 이메일 형식입니다.");
                }
            }
        }
        if (r == 1) {
            req.setAttribute("nickname", nickname);
            req.getRequestDispatcher("/member/signup-success.jsp").forward(req, resp);
        } else {
            req.setAttribute("id", id);
            req.setAttribute("pw", pw);
            req.setAttribute("email", email);
            req.setAttribute("name", name);
            req.setAttribute("nickname", nickname);

            if (foundId != null) {
                req.setAttribute("idERR", "이미 사용중인 아이디 입니다.");
            }
            if (foundNickname != null) {
                req.setAttribute("nicknameERR", "이미 사용중인 닉네임 입니다.");
            }
            req.getRequestDispatcher("/member/signup-fail.jsp").forward(req, resp);
        }
    }
}
