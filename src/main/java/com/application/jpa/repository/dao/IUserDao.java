package com.application.jpa.repository.dao;

import com.application.jpa.domain.User;

public interface IUserDao<T, ID> extends IBaseJpaDao<T, ID> {
    User findById(ID id);
}
