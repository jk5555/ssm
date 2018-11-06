package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

import java.util.List;

public interface UserDao {
    /**
     * 根据用户名或邮箱查找用户
     * @param username
     * @param email
     * @return
     */
    List<User> findRegisterUser(String username, String email);

    /**
     * 保存用户信息
     * @param user
     */
    void saveUser(User user);

    /**
     * 根据激活码查找user
     * @param code
     * @return
     */
    User findUserByCode(String code);

    /**
     * 修改状态码
     * @param user
     */
    void updateStatus(User user);

    /**
     * 根据用户名和密码查找用户
     * @param username
     * @param password
     * @return
     */
    User findLoginUser(String username, String password);
}
