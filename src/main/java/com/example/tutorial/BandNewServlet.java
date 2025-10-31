package com.example.tutorial;

import com.example.tutorial.util.MybatisUtil;
import com.example.tutorial.vo.Band;

import com.example.tutorial.vo.Member;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@WebServlet("/band/new")
public class BandNewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Member logonUser = (Member) req.getSession().getAttribute("logonUser");
        if (req.getSession().getAttribute("logonUser") == null) {
            resp.sendRedirect("/member/login");
            return;
        }

        req.setAttribute("auth", true);
        req.setAttribute("logonUser", req.getSession().getAttribute("logonUser"));
        req.setAttribute("logonUserNickname", logonUser.getNickname());

        req.getRequestDispatcher("/band/new.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Member logonUser = (Member) req.getSession().getAttribute("logonUser");

        String bandName = req.getParameter("bandName");
        String category = req.getParameter("category");


        if (bandName == null || bandName.trim().isEmpty() || category == null || category.trim().isEmpty()) {
            req.setAttribute("error", "밴드 이름과 카테고리는 필수입니다.");
            req.getRequestDispatcher("/band/new.jsp").forward(req, resp);
            return;
        }

        String randomImgPath = "/img/default_band.png";
        ServletContext context = getServletContext();
        String imageFolderPath = context.getRealPath("/img/bandimg");

        File folder = new File(imageFolderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Warning: img 폴더를 찾을 수 없거나 유효하지 않습니다: " + imageFolderPath);
        } else {
            File[] files = folder.listFiles();
            List<String> imageFileNames = new ArrayList<>();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    String fileName = file.getName().toLowerCase();
                    if(file.isFile() && !file.isHidden() && (fileName.endsWith(".png")
                            || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                            || fileName.endsWith(".gif"))) {
                                imageFileNames.add(file.getName());
                    }
                }
            }
            if (imageFileNames.isEmpty()) {
                System.err.println("Warning: img 폴더에 유효한 이미지 파일이 없습니다.");
            } else {
                Random random = new Random();
                int randomIdx = random.nextInt(imageFileNames.size());
                String selectedImageName = imageFileNames.get(randomIdx);
                randomImgPath = "/img/bandimg" + selectedImageName;
            }
        }

        Band band = new Band();
        band.setCreateMaster(logonUser.getId());
        band.setBandName(bandName);
        band.setCategory(category);
        band.setMemberCnt(1);
        band.setBandImgPath(randomImgPath);


        SqlSession sqlSession = MybatisUtil.build().openSession(true);

        try {
            int r = sqlSession.insert("mappers.BandMapper.insertOne", band);
            if (r == 1) {
                int generatedBandNo = band.getNo();
                resp.sendRedirect("/band/board?no=" + generatedBandNo);
            } else {
                resp.sendRedirect("/band/new");
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        } finally {
            sqlSession.close();
        }

    }
}
