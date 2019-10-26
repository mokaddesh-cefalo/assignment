package com.cefalo.assignment.service.orm;

import com.cefalo.assignment.model.orm.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, String> {
    boolean existsUserByUserName(String userName);
}
