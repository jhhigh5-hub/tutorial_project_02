package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Band;
import com.example.tutorial.vo.Posts;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        if (keyword == null || keyword.trim().isEmpty()) {
            req.setAttribute("Error", "검색어를 입력하세요.");
            req.getRequestDispatcher("/search-result.jsp").forward(req, resp);
            return;
        }

        try (SqlSession sqlSession = MybatisUtil.build().openSession()) {
            List<Band> bands = sqlSession.selectList("mappers.SearchMapper.searchBands", "%" + keyword + "%");
            List<Posts> posts = sqlSession.selectList("mappers.SearchMapper.searchPosts", "%" + keyword + "%");

            req.setAttribute("keyword", keyword);
            req.setAttribute("bands", bands);
            req.setAttribute("posts", posts);

            req.getRequestDispatcher("/search-result.jsp").forward(req, resp);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
