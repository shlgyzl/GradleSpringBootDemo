package com.application.repository.jpa.dao.impl;

import com.application.domain.User;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl extends BaseJpaDaoImpl<User, Long> {
    List<Map<String, Object>> findById(String jPQL, Object... objects) {
        User load = this.load(jPQL, objects);
        @SuppressWarnings("unchecked")
        Map<String, Object> map = BeanMap.create(load);
        return Collections.singletonList(map);
    }
}
