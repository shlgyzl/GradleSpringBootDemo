package com.application.repository.jpa.dao;

import com.application.domain.jpa.User;

public interface IUserDao<T, ID> extends IBaseJpaDao<T, ID> {
    User findById(ID id);
}
