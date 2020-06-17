package com.application.jpa.repository.dao;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 自定义Dao层
 *
 * @author yanghaiyong
 */
public interface IBaseJpaDao<T, ID> {
    /**
     * 添加实体类
     *
     * @param t
     * @return T
     */
    T add(T t);

    /**
     * 更新实体类
     *
     * @param t 实体
     * @return T 返回更新的实体
     */
    T update(T t);

    /**
     * 根据主键ID删除实体类
     *
     * @param id 主键id
     */
    void delete(ID id);

    /**
     * 根据jPQL语句更新或删除操作
     *
     * @param jPQL sql
     * @param obj  参数
     * @return int 返回更新的条数
     */
    int executeUpdate(String jPQL, Object... obj);

    /**
     * 根据主键ID查找单个实体类
     *
     * @param id 主键id
     * @return T 返回实体对象
     */
    T load(ID id);


    /**
     * 根据jPQL语句查询单个实体类
     *
     * @param jPQL sql
     * @param obj  参数可有可无
     * @return T
     */
    T load(String jPQL, Object... obj);

    /**
     * 查找所有的实体类
     *
     * @return T
     */
    List<T> findAll();

    /**
     * 根据jPQL语句查询集合实体类
     *
     * @param jPQL sql
     * @param obj  参数可有可无
     * @return List 返回实体集合
     */
    List<T> find(String jPQL, Object... obj);

    /**
     * 聚合查询
     *
     * @param jPQL sql
     * @param obj  参数
     * @return Object
     */
    Object findByAggregate(String jPQL, Object... obj);

    /**
     * 查找总记录数
     *
     * @return int
     */
    int count();

    /**
     * 根据jPQL语句查询记录数
     *
     * @param jPQL sql
     * @param obj  参数
     * @return int
     */
    int count(String jPQL, Object... obj);

    /**
     * 分页查询集合实体类
     *
     * @param page 当前页
     * @param size 单页总数
     * @return List
     */
    List<T> findPage(Integer page, Integer size);

    /**
     * 根据jPQL语句查询集合实体类
     *
     * @param page 当前页
     * @param size 单页总数
     * @param jPQL sql
     * @param obj  参数
     * @return List
     */
    List<T> findPage(Integer page, Integer size, String jPQL, Object... obj);

    /**
     * 根据jPQL语句查询集合实体
     *
     * @param page page实体
     * @param jPQL sql
     * @param obj  参数
     * @return Page
     */
    Page<T> findPage(Page page, String jPQL, Object... obj);
}
