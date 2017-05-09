package com.mjduan.project.util.jdbc.customRowMapper;

import java.util.List;

/**
 * Created by Duan on 2017/2/8.
 */
public class Page<T> {
    //下面这三条数据是最核心的，其它的几个属性是由这个三个属性值计算得来的
    private int currentPage=1;
    //每页显示几条数据
    private int pageSize=10;
    private int recordTotal;

    //总的页数
    private int pageTotal;
    //上一页
    private int previousPage;
    private int nextPage;
    private int firstPage=1;
    private int lastPage;
    private List<T> content;

    public int getCurrentPage() {
        return currentPage<=0?1:currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage <= 0) {
            this.currentPage=1;
        }else {
            this.currentPage = currentPage;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageTotal() {
        return recordTotal%pageSize!=0?recordTotal/pageSize+1:recordTotal/pageSize;
    }

    public int getRecordTotal() {
        return recordTotal;
    }

    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
    }

    public int getPreviousPage() {
        if (currentPage > 1) {
            return currentPage - 1;
        } else {
            return firstPage;
        }
    }

    public int getNextPage() {
        int temp=this.getLastPage();
        if (currentPage < temp) {
            return currentPage + 1;
        } else {
            return temp;
        }
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return this.getPageTotal();
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Page{" +
                "currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", pageTotal=" + pageTotal +
                ", recordTotal=" + recordTotal +
                ", previousPage=" + previousPage +
                ", nextPage=" + nextPage +
                ", firstPage=" + firstPage +
                ", lastPage=" + lastPage +
                ", content=" + content +
                '}';
    }
}
