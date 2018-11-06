package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    private CategoryDao dao = new CategoryDaoImpl();


    @Override
    public List<Category> findAll() {
        //从redis里查询数据
        Jedis jedis = JedisUtil.getJedis();
        Set<Tuple> category = jedis.zrangeWithScores("category", 0, -1);
        List<Category> list = null;
        //判断,
        if (category == null || category.size() == 0) {
            //说明为第一次访问,需要从数据库里查询
            list = dao.findAll();
//            将list排序
            //Collections.sort(list,(a,b)->a.getCid()-b.getCid());
            //根据cid将cname存入redis里面
            for (Category c : list) {
                jedis.zadd("category", c.getCid(), c.getCname());
            }

        } else {
//            从redis里面查,即category有值,但是list肯定为null
            list = new ArrayList<Category>();
            //由于返回类型为list,将set集合转化为list集合
            for (Tuple tuple : category) {
                Category cg = new Category();
                cg.setCid((int) tuple.getScore());
                cg.setCname(tuple.getElement());
                list.add(cg);
            }
        }


        //将数据返回
        return list;
    }


}
