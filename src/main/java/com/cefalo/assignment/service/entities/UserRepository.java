package com.cefalo.assignment.service.entities;

import com.cefalo.assignment.model.orm.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, String> {
    boolean existsUserByUserName(String userName);
}
