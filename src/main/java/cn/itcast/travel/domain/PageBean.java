package cn.itcast.travel.domain;


import java.util.List;

public class PageBean<T> {
    private int totalPage;//总页码
    private int TotalCount;//总记录数
    private int currentPage;//当前页码
    private int pageSize;//页展示条数
    private List<T> routes;//展示信息

    public PageBean() {
    }

    public PageBean(int totalPage, int totalCount, int currentPage, int pageSize, List<T> routes) {
        this.totalPage = totalPage;
        TotalCount = totalCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.routes = routes;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getRoutes() {
        return routes;
    }

    public void setRoutes(List<T> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "totalPage=" + totalPage +
                ", TotalCount=" + TotalCount +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", routes=" + routes +
                '}';
    }
}
