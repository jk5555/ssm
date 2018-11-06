package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Route/*")
public class RouteServlet extends BaseServlet {

    private RouteService service = new RouteServiceImpl();

    public void routeQuery(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取提交的数据
        //获取当前页码
        String currentPageStr = req.getParameter("currentPage");
        //获取cid
        String cidStr = req.getParameter("cid");
        //获取搜索值rname
        String rname = req.getParameter("rname");

        //自定义页面尺寸
        int pageSize = 8;

        //处理rname
        if ("".equals(rname)||"null".equals(rname)) {
            //初始化为null;
            rname = null;
        }
        // else {
        // 不为null就转码为utf-8
        // rname = new String(rname.getBytes("ISO-8859-1"), "UTF-8");
        // }

        //将格式转换为int类型
        int currentPage;
        int cid;
        try {
            currentPage = Integer.parseInt(currentPageStr);
        } catch (NumberFormatException e) {
            //若转化失败,就将它出始化
            currentPage = 1;
        }

        try {
            cid = Integer.parseInt(cidStr);
        } catch (NumberFormatException e) {
            //若转化失败,就将它出始化
            cid = 0;
        }
        //进一步判断值是否正确,增强程序的健壮性

        //校验cid 初始化为0是为了让其查询所有
        if (cid < 1) {
            cid = 0;
        } else if (cid > 8) {
            cid = 0;
        }

        //进一步校验currentPage
        //查询数据库得到最大页码
        int totalPage = service.queryTotalPage(cid, pageSize,rname);
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        if (currentPage < 1) {
            currentPage = 1;
        }

        //调用方法查询得到封装的pagebean对象
        PageBean<Route> page = service.routeQuery(cid, currentPage, pageSize,rname);

        //转化为json对象
        String json = null;
        try {
            json = writeJson(page);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);

    }


    public void queryOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取参数rid
        String ridStr = req.getParameter("rid");
        //对参数进行校验,保证数据的正确性
        int rid;
        try{
            rid = Integer.parseInt(ridStr);
        }catch(Exception e){
            rid=1;
        }

        //调用service层查询得到一个route对象
        Route route = service.queryOne(rid);
        if(route==null) {
            String json = "{}";
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write(json);
            return;
        }

        //转化为json
        String json = writeJson(route);
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);
    }


    //判断是否收藏
    public void favorite(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取参数rid
        String ridStr = req.getParameter("rid");
        //对参数进行校验,保证数据的正确性
        int rid;
        try{
            rid = Integer.parseInt(ridStr);
        }catch(Exception e){
            rid=1;
        }

        //获取uid
        User user = (User) req.getSession().getAttribute("user");

        //判断是否登陆
        if (user == null) {
            //说明未登录
            boolean flag=false;
            String json = writeJson(flag);
            resp.getWriter().write(json);
            return;
        }

        int uid = user.getUid();

        //查询是否收藏
        boolean flag = service.isFavorite(rid,uid);

        String json = writeJson(flag);
        resp.getWriter().write(json);
    }

    //添加收藏
    public void addFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取参数rid
        String ridStr = req.getParameter("rid");
        //对参数进行校验,保证数据的正确性
        int rid;
        try{
            rid = Integer.parseInt(ridStr);
        }catch(Exception e){
            rid=1;
        }

        //获取uid
        User user = (User) req.getSession().getAttribute("user");

        //判断是否登陆
        if (user == null) {
            //说明未登录
            boolean flag=false;
            String json = writeJson(flag);
            resp.getWriter().write(json);
            return;
        }

        int uid = user.getUid();
        //调用service层添加
        service.addFavorite(rid, uid);
        boolean flag=true;
        String json = writeJson(flag);
        resp.getWriter().write(json);

    }
}
