package com.application.repository.jpa.dao.impl;

import com.application.domain.jpa.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Repository
public class UserDaoImpl extends BaseJpaDaoImpl<User, Long> {
    List<Map<String, Object>> findById(String jPQL, Object... objects) {
        User load = this.load(jPQL, objects);
        Map<String, Object> map = BeanMap.create(load);
        return Collections.singletonList(map);
    }

    List<Map<String, Object>> findByLogin(String login) {
        List<Map<String, Object>> result = new ArrayList<>();
        Session session = getSession();
        String sql = "select U.id,U.login,U.password from User U where U.login like %:login% ";
        Query query = session.createQuery(sql, User.class).setParameter("login", login);
        List<User> list = query.list();
        list.forEach(n -> result.add(BeanMap.create(n)));
        return result;
    }
}
