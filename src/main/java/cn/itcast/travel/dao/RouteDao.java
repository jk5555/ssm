package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    int queryTotalCount(int cid, String rname);

    List<Route> queryRoute(int cid, int start, int pageSize, String rname);

    Route queryOne(int rid);
}
