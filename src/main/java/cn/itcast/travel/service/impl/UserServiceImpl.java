package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class UserServiceImpl implements UserService {

    UserDao dao = new UserDaoImpl();

    /**
     * 查询user信息是否存在,并将存在与否的信息封装为json字符串
     *
     * @param user
     * @return
     */
    @Override
    public String registerUser(User user) {
        //调用业务层findUser()方法查询user是否存在,根据邮箱和用户名判断
        String username = user.getUsername();
        String email = user.getEmail();
        //创建响应信息对象
        ResultInfo resultInfo = new ResultInfo();
        List<User> list = dao.findRegisterUser(username, email);

        if (list!=null&&list.size() != 0) {
            //说明用户已存在 不能注册
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("注册失败,用户已存在!");
        } else {
            //说明用户不存在,准许注册
            //设置状态码为N
            user.setStatus("N");
            //设置code
            String uuid = UuidUtil.getUuid();
            user.setCode(uuid);
            dao.saveUser(user);
            //发送邮件
            MailUtils.sendMail(user.getEmail(), "<a href='http://localhost/travel/ActiveCodeServlet?code="+uuid+"'>黑马旅游网(点击激活)</a>", "激活邮件");

            //向响应信息对象里面存入信息
            resultInfo.setFlag(true);
            resultInfo.setData("恭喜你注册成功!");
        }
//        将信息对象转化为json对象
        ObjectMapper mapper = new ObjectMapper();
        String json=null;
        try{
            json = mapper.writeValueAsString(resultInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public ResultInfo findUserByCode(String code) {
        //查询数据库,得到user对象
        User user = dao.findUserByCode(code);
        ResultInfo resultInfo = new ResultInfo();

        if (user == null) {
            //说明用户不存在,激活码不一致,激活失败
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("激活失败,请重试或联系管理员!");
        } else {
            //进一步判断是否是第一次点击激活链接
            if ("Y".equals(user.getStatus())) {
                //说明用户已经激活,不必在进行修改数据库的操作
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("你已经激活过了,直接去<a href='http://localhost/travel/login.html'>登陆</a>吧!");
            } else {
                //说明用户未激活,访问数据库修改状态码
                user.setStatus("Y");
                dao.updateStatus(user);
                resultInfo.setFlag(true);
                resultInfo.setData("你已成功激活邮箱,去<a href='http://localhost/travel/login.html'>登陆</a>");
            }
        }

        return resultInfo;
    }

    @Override
    public User findLoginUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        //调用dao的方法查询用户
        User loginUser = dao.findLoginUser(username,password);
        if (loginUser!=null&&"Y".equals(loginUser.getStatus())) {
            //说明用户已激活,且登陆信息正确.
            return loginUser;
        }
        return null;
    }
}
