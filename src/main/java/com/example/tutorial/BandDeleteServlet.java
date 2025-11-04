package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Band;
import com.example.tutorial.vo.Member;
import com.example.tutorial.vo.Posts;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/band/delete")
public class BandDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // no로 넘어오는 파라미터 넘어온 값 뽑아서
        int bandNo = Integer.parseInt(req.getParameter("bandNo"));

        // mybatis 연결해서 deleteByNo 호출
        SqlSession sqlSession = MybatisUtil.build().openSession(false);
        // 삭제 전에 처리해야될 일들
        // 1. 요청자가 쓴 글인 경우에만 삭제가 되게 해야되고
        Member user = (Member) req.getSession().getAttribute("logonUser");

        Band band = sqlSession.selectOne("mappers.BandMapper.selectByNo", bandNo);

        List<Posts> posts = sqlSession.selectList("mappers.PostsMapper.selectAllPostsByBandNo", bandNo);
        for (Posts post : posts) {
            sqlSession.delete("mappers.PostsLikeMapper.deleteLikesByPostsNo", post.getNo());
            sqlSession.delete("mappers.PostsMapper.deleteCommentsByPostNo", post.getNo());
            sqlSession.delete("mappers.PostsMapper.deletePostByNo", post.getNo());
        }
        sqlSession.delete("mappers.BandJoinRequestMapper.deleteRequestsByBandNo", bandNo);

        int r = sqlSession.delete("mappers.BandMapper.deleteBandByNo", bandNo);

        if (r > 0) {
            sqlSession.commit();
        } else {
            sqlSession.rollback();
        }
        sqlSession.close();

        resp.sendRedirect("/main");

    }
}
