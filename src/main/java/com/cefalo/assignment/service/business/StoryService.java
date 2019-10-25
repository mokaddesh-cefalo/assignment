package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.Story;

import java.util.List;
import java.util.Optional;

public interface StoryService {
    Optional<Story> postStoryObject(Story story);
    List<Story> getAllStoryJson();
}
