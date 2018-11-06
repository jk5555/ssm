package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite queryFavorite(int rid, int uid) {
        String sql = "select * from tab_favorite where rid=? and uid=? ";
        Favorite favorite = null;
        try {
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);

        } catch (DataAccessException e) {
            // e.printStackTrace();
            System.out.println("uid为  " + uid + "  rid为  " + rid + "  的路线没被收藏!");
        }
        return favorite;
    }

    @Override
    public int queryFavoriteCountByRid(int rid) {
        //查询收藏此线路被登陆用户收藏的次数
        String sql = "select count(uid) from tab_favorite where rid = ? ";
        int count = 0;
        try {
            count = template.queryForObject(sql, int.class, rid);
        } catch (DataAccessException e) {
            //e.printStackTrace();
        }
        return count;
    }

    @Override
    public void addFavorite(int rid, int uid) {
        String sql="insert into tab_favorite values(?,?,?)";
        template.update(sql, rid, new Date(), uid);
    }
}
