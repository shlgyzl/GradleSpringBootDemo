package com.application.web.repository.jpa.dao;

import com.application.web.domain.jpa.User;

public interface IUserDao<T, ID> extends IBaseJpaDao<T, ID> {
    User findById(ID id);
}
