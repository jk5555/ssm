package cn.itcast.travel.web.servlet.loginservlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        //校验验证码
        String checkCode = req.getParameter("check");
        //从session域里获取验证码
        HttpSession session = req.getSession();
        //noinspection AlibabaLowerCamelCaseVariableNaming
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(checkCode)) {
            //说明验证码错误
            String json = "{\"flag\":false,\"errorMsg\":\"验证码填写错误!\"}";
            resp.getWriter().write(json);
            return;
        }
        //获取所有提交数据
        Map<String, String[]> map = req.getParameterMap();
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
        //调用业务层返回user对象
        User loginUser = userService.findLoginUser(user);
        //获取存贮提示信息的resultinfo对象
        ResultInfo resultInfo = new ResultInfo();
        //根据此对象进行校验
        if (loginUser != null) {
            //将loginUser对象存入session里面去
            session.setAttribute("user", loginUser);
            //说明信息正确,进一步校验是否记住密码
            if (map.get("auto_login") != null) {
                //说明勾选了 记住密码,需要设置cookie
                //让session存活30分钟
                Cookie cookie1 = new Cookie("JSESSIONID", session.getId());
                cookie1.setMaxAge(60 * 30);
                resp.addCookie(cookie1);
            }
            //将正确信息提示封装到resultinfo对象里去
            resultInfo.setFlag(true);
            resultInfo.setData("登陆成功!");
        } else {
            //说明信息有误,提示  错误信息,将错误信息提示封装到resultinfo对象里去
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("登陆失败,用户未激活或者用户名或密码错误!");
        }


        //      将信息对象转化为json对象
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(resultInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }


        resp.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
