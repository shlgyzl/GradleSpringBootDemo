package com.application.repository.jpa;

import com.application.domain.jpa.Attachment;
import com.application.domain.jpa.QAttachment;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import static com.application.web.resources.util.RepositoryUtil.common;

public interface AttachmentRepository extends BaseJpaRepository<Attachment, Long>,
        QuerydslBinderCustomizer<QAttachment> {
    @Override
    default void customize(QuerydslBindings bindings, QAttachment root) {
        common(bindings, root.id, root.fileName);
    }
}

