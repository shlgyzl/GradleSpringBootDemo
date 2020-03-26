package com.application.controller.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;


public final class DomainUtil {

    private static BeanWrapper src;
    private static BeanWrapper dest;

    private static void createBeanWrapper(Object source, Object target) {
        src = new BeanWrapperImpl(source);
        dest = new BeanWrapperImpl(target);
    }

    /**
     * 前端参数去null的浅循环(如果参数中存在集合,那么集合为null时表示不改变原有数据,size为0时表示清除该关联集合数据)
     * 缺点：无法对集合持久化,无法对属性对象持久化,只能简单对第一层进行过滤,不能过滤集合,不能深度过滤集合
     *
     * @param source 参数实体
     * @param target 目标实体
     */
    public static <E, T> T copy(E source, T target) {
        // 建议后期将此创建放置于缓存中,如果有大量相同的对象复制一定要从缓存获取提高性能
        try {
            BeanCopier beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
            createBeanWrapper(source, target);
            PropertyDescriptor[] sourceDescriptors = src.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : sourceDescriptors) {
                if (Objects.equals("class", propertyDescriptor.getDisplayName())) {
                    continue;
                }
                Object sourceValue = src.getPropertyValue(propertyDescriptor.getName());
                Object destValue = dest.getPropertyValue(propertyDescriptor.getName());
                if (ClassUtils.isAssignable(Collection.class, propertyDescriptor.getPropertyType())) {
                    Type genericType = source.getClass().getDeclaredField(propertyDescriptor.getName()).getGenericType();
                    boolean mFlag = false;
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType type = (ParameterizedType) genericType;
                        Type[] arguments = type.getActualTypeArguments();
                        for (Type argument : arguments) {
                            Class<?> name = ClassUtils.forName(argument.getTypeName(), ClassUtils.getDefaultClassLoader());
                            if (!name.getPackage().getName().contains("com.application.domain")) {
                                mFlag = true;
                            }
                        }
                    }
                    Collection sourceCollection = (Collection) sourceValue;
                    Collection targetCollection = (Collection) destValue;
                    // 其他引用对象
                    if (mFlag) {
                        sourceCollection.addAll(targetCollection);
                        continue;
                    }
                    if(ObjectUtils.isEmpty(sourceCollection)){
                        src.setPropertyValue(new PropertyValue(propertyDescriptor.getName(), null));
                    }
                    if (!ObjectUtils.isEmpty(sourceCollection)) {
                        if (ObjectUtils.isEmpty(targetCollection)) {
                            src.setPropertyValue(new PropertyValue(propertyDescriptor.getName(), destValue));
                        }
                        if (!ObjectUtils.isEmpty(targetCollection)) {
                            Collection temp = new HashSet();
                            Iterator iterator = sourceCollection.iterator();
                            while (iterator.hasNext()) {
                                Object sourceField = iterator.next();
                                for (Object targetField : targetCollection) {
                                    if (Objects.equals(getId(sourceField), getId(targetField))) {
                                        copy(sourceField, targetField);
                                        continue;
                                    }
                                    temp.add(targetField);
                                }
                            }
                            sourceCollection.addAll(temp);
                        }
                    }
                }
                // null、version字段、集合都不会进行copy
                if (Objects.isNull(sourceValue)
                        || Objects.equals("version", propertyDescriptor.getDisplayName())) {
                    src.setPropertyValue(new PropertyValue(propertyDescriptor.getName(), destValue));
                }
            }
            beanCopier.copy(src.getWrappedInstance(), target, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    public static Object getId(Object source_) throws IllegalAccessException, NoSuchFieldException {
        Field id = source_.getClass().getDeclaredField("id");
        id.setAccessible(true);
        return id.get(source_);
    }

    public static Type getType(Type targetGenericType) {
        if (targetGenericType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) targetGenericType;
            // 获取集合的泛型实参
            Type[] arguments = type.getActualTypeArguments();
            if (arguments.length == 1) {
                return arguments[0];
            }
        }
        return null;
    }
}
