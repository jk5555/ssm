package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UserDaoImpl implements UserDao {

    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());


    @Override
    public List<User> findRegisterUser(String username, String email) {
        List<User> list= null;
        try {
            String sql = "select * from tab_user where username=? or email=?";
            //这里需要注意一下,若找不到user信息,会直接报异常,所以需要try...catch一下
            list = template.query(sql, new BeanPropertyRowMapper<User>(User.class),username,email);
        } catch (Exception e) {
            System.out.println("执行了..........");
        }
        return list;
    }

    @Override
    public void saveUser(User user) {
        String sql = "insert into tab_user values(null,?,?,?,?,?,?,?,?,?);";
        template.update(sql,user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());
    }

    @Override
    public User findUserByCode(String code) {
        User user = null;
        try {
            String sql = "select * from tab_user where code=?";
            //这里需要注意一下,若找不到user信息,会直接报异常,所以需要try...catch一下
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class),code);
        } catch (Exception e) {

        }
        return user;
    }

    @Override
    public void updateStatus(User user) {
        String sql = "update tab_user set status=? where code = ? ";
        template.update(sql, user.getStatus(), user.getCode());
    }

    @Override
    public User findLoginUser(String username, String password) {
        User user = null;
        try {
            String sql = "select * from tab_user where username=? and password=?";
            //这里需要注意一下,若找不到user信息,会直接报异常,所以需要try...catch一下
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class),username,password);
        } catch (Exception e) {

        }
        return user;
    }
}
