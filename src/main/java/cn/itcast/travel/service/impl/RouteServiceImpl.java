package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.*;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {

    private RouteDao routeDao = new RouteDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public int queryTotalPage(int cid, int pageSize, String rname) {
        //查询总记录数
        int totalCount = routeDao.queryTotalCount(cid,rname);
        //计算总页面
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        return totalPage;
    }

    @Override
    public PageBean<Route> routeQuery(int cid, int currentPage, int pageSize, String rname) {
        //查询总记录数
        int totalCount = routeDao.queryTotalCount(cid, rname);
        //计算总页面
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;

//        计算当前页数据开始条数
        int start = (currentPage-1)*pageSize;
        //查询数据库获得当页数据
        List<Route> list = routeDao.queryRoute(cid,start,pageSize,rname);

        //封装对象
        PageBean<Route> routePageBean = new PageBean<>();
        routePageBean.setCurrentPage(currentPage);
        routePageBean.setPageSize(pageSize);
        routePageBean.setTotalCount(totalCount);
        routePageBean.setTotalPage(totalPage);
        routePageBean.setRoutes(list);
        return routePageBean;
    }

    @Override
    public Route queryOne(int rid) {
        //先查询route
        Route route = routeDao.queryOne(rid);
        if(route==null){
            return null;
        }
        //查询seller和routeImg并封装到route对象里去
        Seller seller = sellerDao.queryOne(route.getSid());
        route.setSeller(seller);

        List<RouteImg> list = routeImgDao.queryOne(rid);
        route.setRouteImgList(list);

        int count = favoriteDao.queryFavoriteCountByRid(rid);
        route.setCount(count);

        return route;
    }

    @Override
    public boolean isFavorite(int rid, int uid) {
        Favorite f = favoriteDao.queryFavorite(rid,uid);
        if(f==null){
            //说明未收藏
            return false;
        }else{
            //说明收藏
            return true;
        }
    }

    @Override
    public void addFavorite(int rid, int uid) {
        favoriteDao.addFavorite(rid,uid);
    }
}
