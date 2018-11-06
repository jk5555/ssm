package cn.itcast.travel.service;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;

public interface UserService {

    /**
     * 查询user信息是否存在,并将存在与否的信息封装为json字符串
     * @param user
     * @return
     */
    String registerUser(User user);

    /**
     * 根据激活码查询用户对象,并对对象进行处理,返回相应信息封装到resultinfo对象中返回
     * @param code
     * @return
     */
    ResultInfo findUserByCode(String code);

    /**
     * 根据封装的user对象里面的信息查找真实用户信息user对象
     * @param user
     * @return
     */
    User findLoginUser(User user);
}
