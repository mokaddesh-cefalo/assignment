package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.orm.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoryServiceImpl implements StoryService{
    @Autowired StoryRepository storyRepository;

    @Override
    public Optional<Story> postStoryObject(Story story) {
        return Optional.ofNullable(storyRepository.save(story));
    }

    @Override
    public List<Story> getAllStory(){
        List<Story> stories = new ArrayList<>();
        storyRepository.findAll().forEach(story -> stories.add(story));
        return stories;
    }

    @Override
    public Optional<Story> getStoryById(Long storyId){
        return storyRepository.findById(storyId);
    }


}
