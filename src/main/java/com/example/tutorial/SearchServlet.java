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
        if (req.getSession().getAttribute("logonUser") != null) {
            req.setAttribute("auth", true);
        } else {
            req.setAttribute("auth", false);
        }

        int page = req.getParameter("page") != null ?
                Integer.parseInt(req.getParameter("page")) : 1;
        String keyword = req.getParameter("keyword") != null ? req.getParameter("keyword") : "";

        SqlSession sqlSession = MybatisUtil.build().openSession(true);

        Map param = Map.of("offset", (page - 1) * 10,
                "keyword", "%" + keyword + "%");
        List<Band> band =
                sqlSession.selectList("mappers.BandMapper.selectKeywordByOffset", param);


        // int count = sqlSession.selectOne("mappers.ArticleMapper.countAll");
        int count = sqlSession.selectOne("mappers.BandMapper.selectPendingBandJoinRequests",
                "%"+keyword+"%");
        int lastPage = count / 10 + (count % 10 > 0 ? 1 : 0);



        req.setAttribute("lastPage", lastPage);
        req.setAttribute("page", page);
        req.setAttribute("count", count);
        req.setAttribute("keyword", keyword);


        req.getRequestDispatcher("/board.jsp").forward(req, resp);

    }
}
