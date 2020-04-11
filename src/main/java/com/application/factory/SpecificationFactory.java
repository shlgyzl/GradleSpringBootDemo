package com.application.factory;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;

public final class SpecificationFactory {

    public static Specification localDateTimeBetween(String attribute, LocalDateTime min, LocalDateTime max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(attribute), min, max);
    }

    public static Specification localDateBetween(String attribute, LocalDate min, LocalDate max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(attribute), min, max);
    }

    public static Specification localTimeBetween(String attribute, LocalTime min, LocalTime max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(attribute), min, max);
    }

    public static Specification zonedDateTimeBetween(String attribute, ZonedDateTime min, ZonedDateTime max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(attribute), min, max);
    }

    public static Specification dateBetween(String attribute, Date min, Date max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(attribute), min, max);
    }

    public static Specification equal(String attribute, Object value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(attribute), value);
    }

    public static Specification containsLike(String attribute, String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(attribute), "%" + value + "%");
    }

    public static Specification in(String attribute, Collection c) {
        return (root, query, criteriaBuilder) -> root.get(attribute).in(c);
    }

    public static Specification greatThan(String attribute, BigDecimal value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(attribute), value);
    }

    public static Specification greatThan(String attribute, Long value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(attribute), value);
    }

    public static Specification greatThan(String attribute, Integer value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(attribute), value);
    }

    public static Specification lessThan(String attribute, BigDecimal value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(attribute), value);
    }

    public static Specification lessThan(String attribute, Integer value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(attribute), value);
    }

    public static Specification lessThan(String attribute, Long value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(attribute), value);
    }
}
