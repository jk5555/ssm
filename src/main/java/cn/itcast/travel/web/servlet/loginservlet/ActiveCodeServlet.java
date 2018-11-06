package cn.itcast.travel.web.servlet.loginservlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
@WebServlet("/ActiveCodeServlet")
public class ActiveCodeServlet extends HttpServlet {
    @Override
    @SuppressWarnings("AliMissingOverrideAnnotation")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");

        //获取激活码
        String code = req.getParameter("code");
        //根据code查询用户是否存在,根据返回的resultinfo信息对象贮存的信息判断激活成功与否
        UserService userService = new UserServiceImpl();
         ResultInfo resultInfo = userService.findUserByCode(code);
        //进行判断
        if (resultInfo.isFlag()) {
            //说明激活成功,且是真实用户
            resp.getWriter().write(resultInfo.getData()+"");
        }else{
            //说明激活失败
            resp.getWriter().write(resultInfo.getErrorMsg()+"");

        }

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
