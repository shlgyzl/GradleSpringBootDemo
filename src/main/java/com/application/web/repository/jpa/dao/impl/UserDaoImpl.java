package com.application.web.repository.jpa.dao.impl;

import com.application.web.domain.jpa.User;
import com.application.web.repository.jpa.dao.IUserDao;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Repository
public class UserDaoImpl extends BaseJpaDaoImpl<User, Long> implements IUserDao<User, Long> {

    public User findById(Long id) {
        String jPQL = "select U from User U where U.id = :id ";
        return this.load(jPQL, id);
    }

    List<Map<String, Object>> findAll(String login) {
        List<Map<String, Object>> result = new ArrayList<>();
        Session session = getSession();
        String sql = "select U.id,U.login,U.password from User U where U.login like %:login% ";
        Query query = session.createQuery(sql, User.class).setParameter("login", login);
        List<User> list = query.list();
        list.forEach(n -> result.add(BeanMap.create(n)));
        return result;
    }
}
