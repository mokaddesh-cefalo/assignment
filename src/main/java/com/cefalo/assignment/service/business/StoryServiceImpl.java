package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.orm.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class StoryServiceImpl implements StoryService{
    @Autowired StoryRepository storyRepository;

    public Optional<Story> PostStoryObject(Story story) throws Exception{
        return Optional.ofNullable(storyRepository.save(story));
    }
}
