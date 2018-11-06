package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {
    Favorite queryFavorite(int rid, int uid);

    int queryFavoriteCountByRid(int rid);

    void addFavorite(int rid, int uid);
}
