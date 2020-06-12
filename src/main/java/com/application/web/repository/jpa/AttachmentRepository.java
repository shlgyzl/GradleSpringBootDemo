package com.application.web.repository.jpa;

import com.application.web.domain.jpa.Attachment;
import com.application.web.domain.jpa.QAttachment;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import static com.application.common.util.RepositoryUtil.common;

public interface AttachmentRepository extends BaseJpaRepository<Attachment, Long>,
        QuerydslBinderCustomizer<QAttachment> {
    @Override
    default void customize(QuerydslBindings bindings, QAttachment root) {
        common(bindings, root.id, root.fileName);
    }
}

