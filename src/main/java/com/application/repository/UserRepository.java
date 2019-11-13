package com.application.repository;

import com.application.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select u from User u left join u.dams d where d.name = :name ")
    List<User> findByDamsNamesss(@Param("name") String name);


}
