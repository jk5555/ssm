package cn.itcast.travel.web.servlet.loginservlet;

import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("ALL")
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");

        //校验验证码
        String checkcode = req.getParameter("check");
        HttpSession session = req.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(checkcode)) {
            //说明验证码不正确
            String json = "{\"flag\":false,\"errorMsg\":\"验证码填写错误!\"}";
            resp.getWriter().write(json);
            return;
        }
        //获取参数
        Map<String, String[]> map = req.getParameterMap();
        //遍历map集合,二次非空校验
        Set<String> keys = map.keySet();
        for (String key : keys) {
            String[] values = map.get(key);
            if (values == null || "".equals(values)) {
                String json = "{\"flag\":false,\"errorMsg\":\"信息填写有误!\"}";
                resp.getWriter().write(json);
                return;
            }
        }
//      用工具封装user
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用业务层查询user是否存在,返回响应信息(json字符串)
        UserService userService = new UserServiceImpl();
        String json = userService.registerUser(user);
        //将json返回给客户端
        resp.getWriter().write(json);


    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
