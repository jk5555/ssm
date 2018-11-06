package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/Category/*")
public class CategoryServlet extends BaseServlet {


     private CategoryService service= new CategoryServiceImpl();

    public void category(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //调用service层的findall方法,获取分类信息
        List<Category> list=service.findAll();
        //将list集合转换为json
        String json = writeJson(list);
        System.out.println("json = " + json);
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);

    }


}
