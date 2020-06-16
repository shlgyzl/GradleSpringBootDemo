package com.application.jpa.repository;

import com.application.jpa.common.util.RepositoryUtil;
import com.application.jpa.domain.Attachment;
import com.application.jpa.domain.QAttachment;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface AttachmentRepository extends BaseJpaRepository<Attachment, Long>,
        QuerydslBinderCustomizer<QAttachment> {
    @Override
    default void customize(QuerydslBindings bindings, QAttachment root) {
        RepositoryUtil.common(bindings, root.id, root.fileName);
    }
}

