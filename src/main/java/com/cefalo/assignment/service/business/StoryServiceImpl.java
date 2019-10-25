package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.orm.StoryRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoryServiceImpl implements StoryService{
    @Autowired StoryRepository storyRepository;
    @Autowired StoryUpdateAndDeleteSerive storyUpdateAndDeleteSerive;

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

    @Override
    public Optional<Story> updateStoryById(Long storyId, Story newVersionOfStory) throws Exception{
        return storyUpdateAndDeleteSerive.handleStoryUpdate(storyId, newVersionOfStory) ;
    }

    @Override
    public void deleteStoryById(Long storyId) throws Exception{
        storyUpdateAndDeleteSerive.handleStoryDelete(storyId);
    }
}
