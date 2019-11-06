package com.cefalo.assignment.repositories;

import com.cefalo.assignment.model.orm.Story;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StoryRepository extends PagingAndSortingRepository<Story, Long> {
}
