package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Comment;
import com.example.tutorial.vo.Member;
import com.example.tutorial.vo.Posts;
import com.example.tutorial.vo.PostsLike;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

@WebServlet("/band/post/delete")
public class BandPostDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // no로 넘어오는 파라미터 넘어온 값 뽑아서
        int postNo = Integer.parseInt(req.getParameter("postNo"));
        int bandNo = Integer.parseInt(req.getParameter("bandNo"));

        // mybatis 연결해서 deleteByNo 호출
        SqlSession sqlSession = MybatisUtil.build().openSession(false);
        // 삭제 전에 처리해야될 일들
        // 1. 요청자가 쓴 글인 경우에만 삭제가 되게 해야되고
        Member user = (Member) req.getSession().getAttribute("logonUser");

        System.out.println("로그인 유저 객체 타입: " + user.getClass().getName());
        System.out.println("로그인 유저의 ID 값: " + user.getId());
        System.out.println("로그인 유저의 ID 타입: " + user.getId().getClass().getName());

        Posts posts = sqlSession.selectOne("mappers.PostsMapper.selectPostsByNo", postNo);

        // 3. 연속된 작업을 해야하는 경우이므로 트라이캐치로 커밋, 롤벡 작업 해줘야함.!!!!!!!
        try {
            // 2. 삭제하고자하는 글을 좋아요, 댓글 등의 정보들을 전부 삭제해야지만 글이 삭제됨.
            if (user != null && posts != null && posts.getWriterId().equals(user.getId())) {
                int r = sqlSession.delete("mappers.PostsLikeMapper.deleteLikesByPostsNo", postNo);
                System.out.println("DEBUG: deletedLikes (삭제된 좋아요 수): " + r);
                int r2 = sqlSession.delete("mappers.PostsMapper.deleteCommentsByPostNo", postNo);
                System.out.println("postNo: " + postNo);
                System.out.println("DEBUG: deletedComments (삭제된 댓글 수): " + r2);
                int r3 = sqlSession.delete("mappers.PostsMapper.deletePostByNo", postNo);
                System.out.println("DEBUG: deletedPost (삭제된 게시글 수): " + r3);
            }
            sqlSession.commit();
        } catch (Exception e) {
            System.out.println(e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }

        // 작업이 끝나면... community로 리다이렉트
        resp.sendRedirect("/band/board?no=" + bandNo);


    }
}
