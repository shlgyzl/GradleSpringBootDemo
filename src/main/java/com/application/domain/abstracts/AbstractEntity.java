package com.application.domain.abstracts;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;

@MappedSuperclass
public abstract class AbstractEntity<ID> implements Persistable<ID> {
    @Transient
    @javax.persistence.Transient
    @org.mongodb.morphia.annotations.Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

}
