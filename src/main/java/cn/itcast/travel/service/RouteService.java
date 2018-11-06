package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {


    int queryTotalPage(int cid, int pageSize, String rname);

    PageBean routeQuery(int cid, int currentPage, int pageSize, String rname);

    Route queryOne(int rid);

    boolean isFavorite(int rid, int uid);

    void addFavorite(int rid, int uid);
}
