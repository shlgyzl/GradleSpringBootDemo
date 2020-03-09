package com.application.listener;

import com.application.domain.abstracts.Automatic;
import com.mongodb.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.persistence.Id;
import java.util.Objects;

@Component
@Slf4j
public class MongoDBAutomaticEventListener extends AbstractMongoEventListener<Object> {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoDBAutomaticEventListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void onApplicationEvent(MongoMappingEvent<?> event) {
        super.onApplicationEvent(event);
    }

    @Override
    public void onBeforeConvert(@NonNull BeforeConvertEvent<Object> event) {
        log.debug("新增Mongodb实体：{}", event.getCollectionName());
        final Object source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            if (field.isAnnotationPresent(javax.persistence.Id.class) && field.get(source) == null) {
                field.set(source, getId(event.getCollectionName()));
            }
        });
    }

    private Long getId(final String collName) {
        Query query = new Query(Criteria.where("collectionName").is(collName));
        Update update = new Update();
        update.inc("seqId", 1);
        // 原子操作 查询修改
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        return Objects.requireNonNull(mongoTemplate.findAndModify(query, update, options, Automatic.class)).getSeqId();
    }

    @Override
    public void onBeforeSave(@NonNull BeforeSaveEvent<Object> event) {
        super.onBeforeSave(event);
        log.debug("保存集合之前：{}", event.getCollectionName());
    }

    @Override
    public void onAfterSave(@NonNull AfterSaveEvent<Object> event) {
        super.onAfterSave(event);
        log.debug("保存集合之后：{}", event.getCollectionName());
    }

    @Override
    public void onAfterLoad(@NonNull AfterLoadEvent<Object> event) {
        super.onAfterLoad(event);
        log.debug("加载集合之前：{}", event.getCollectionName());
    }

    @Override
    public void onAfterConvert(@NonNull AfterConvertEvent<Object> event) {
        super.onAfterConvert(event);
        log.debug("转换集合之后：{}", event.getCollectionName());
    }

    @Override
    public void onAfterDelete(@NonNull AfterDeleteEvent<Object> event) {
        super.onAfterDelete(event);
        log.debug("删除集合之后：{}", event.getCollectionName());
    }

    @Override
    public void onBeforeDelete(@NonNull BeforeDeleteEvent<Object> event) {
        super.onBeforeDelete(event);
        log.debug("删除集合之前：{}", event.getCollectionName());
    }
}
