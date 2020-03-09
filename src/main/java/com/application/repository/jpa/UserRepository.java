package com.application.repository.jpa;


import com.application.domain.jpa.QUser;
import com.application.domain.jpa.User;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import javax.persistence.LockModeType;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseJpaRepository<User, Long>, QuerydslBinderCustomizer<QUser> {

    @Query(value = "select U from User U " +
            "left join U.dams D " +
            "where D.name = :name ")
    List<User> findByDamsName(@Param("name") String name);

    @EntityGraph(attributePaths = {"dams"})
    Iterable<User> findAll(Predicate predicate);

    @Query(value = "select U from User U " +
            "where U.id in ( :ids) ")
    List<User> findByIds(@Param("ids") Collection<Long> ids);

    @SuppressWarnings("NullableProblems")
    @EntityGraph(attributePaths = {"dams"})
    Page<User> findAll(@Nullable Predicate predicate, @Nullable Pageable pageable);

    @SuppressWarnings("NullableProblems")
    @EntityGraph(attributePaths = {"dams"})
    Page<User> findAll(Pageable pageable);

    @SuppressWarnings("NullableProblems")
    @Override
    default void customize(QuerydslBindings bindings, QUser user) {
        // 只要使用到了Predicate,那么此处的规则可以我们自己定义
        // 账号包含查询
        //bindings.bind(user.login).first(StringExpression::contains);
        bindings.bind(user.id).first(SimpleExpression::eq);
        // 其他字符串格式的忽略大小写查询
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.excluding(user.password);
    }

    /**
     * 使用此注解在查询方法的时候可以放弃懒加载实体
     *
     * @param id 主键id
     * @return Optional
     */
    @EntityGraph(attributePaths = {"dams"})
    @NotNull
    // 这种锁也只能在个JVM生效,针对多JVM仍然失效
    @Lock(LockModeType.READ)
    Optional<User> findById(@NotNull Long id);

    @Query(value = "SELECT * FROM TBL_USER WHERE ID = ?1", nativeQuery = true)
    User findByIdForNative(Long id);

    /**
     * 本地分页查询
     *
     * @param login    账号
     * @param pageable 分页
     * @return Page
     */
    @Query(value = "SELECT * FROM TBL_USER WHERE LOGIN LIKE %?1% ",
            countQuery = "SELECT count(*) FROM TBL_USER WHERE LOGIN LIKE %?1% ",
            nativeQuery = true)
    Page<User> findByLogin(String login, Pageable pageable);

    /**
     * 排序查询
     *
     * @param login 账号
     * @param sort  排序
     * @return List
     */
    @Query("select U from User U where U.login like ?1%")
    List<User> findByAndSort(String login, Sort sort);

    /**
     * 修改操作
     *
     * @param login 账号
     * @param id    主键id
     * @return int
     */
    @Modifying
    @Query("update User U set U.login = ?1 where U.id = ?2")
    int updateForStatic(String login, Long id);

    /**
     * 普通单列查询
     *
     * @param login 账户id
     * @param sort  排序
     * @return List
     */
    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query("select U.id, LENGTH(U.login) as fn_len from User U where U.login like ?1%")
    List<Object[]> findByAsArrayAndSort(String login, Sort sort);

    Optional<User> findByLogin(String login);
}
