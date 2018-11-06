package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public int queryTotalCount(int cid, String rname) {
        //String sql = "select count(*) from tab_route where cid = ?";
        //定义初始sql
        String sql="select count(*) from tab_route where 1 = 1 ";
        StringBuilder sb = new StringBuilder();
        List list = new ArrayList();

        if (cid!=0) {
            //说明cid有值
            sb.append(" and cid = ? ");
            list.add(cid);
        }

        if (rname != null) {
            //说明rname有值
            sb.append(" and rname like ? ");
            list.add("%"+rname+"%");
        }
        sql+=sb.toString();
        Integer totalCount = template.queryForObject(sql, Integer.class, list.toArray());
        return totalCount;
    }

    @Override
    public List<Route> queryRoute(int cid, int start, int pageSize, String rname) {
        //String sql = "select * from tab_route where cid = ? limit ?,?";
        //定义初始sql
        String sql="select * from tab_route where 1 = 1 ";
        StringBuilder sb = new StringBuilder();
        List list = new ArrayList();

        if (cid!=0) {
            //说明cid有值
            sb.append(" and cid = ? ");
            list.add(cid);
        }

        if (rname != null) {
            //说明rname有值
            sb.append(" and rname like ? ");
            list.add("%"+rname+"%");
        }

        sb.append(" limit ?,?");
        list.add(start);
        list.add(pageSize);
        sql+=sb.toString();
        List<Route> rlist = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class),list.toArray());
        return rlist;
    }

    @Override
    public Route queryOne(int rid) {
        String sql = "select * from tab_route where rid=?";
        Route route = null;
        try {
            route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
        } catch (DataAccessException e) {
            //e.printStackTrace();
        }
        return route;
    }
}
