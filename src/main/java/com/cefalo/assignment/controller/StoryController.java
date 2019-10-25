package com.cefalo.assignment.controller;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.business.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    StoryService storyService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Story postStoryObject(@RequestBody Story story){
        return storyService.postStoryObject(story).orElse(story);
    }

    @GetMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Story> getAllStory(){
        return storyService.getAllStory();
    }

    @GetMapping("/{storyId}")
    public ResponseEntity getStoryById(@PathVariable(value = "storyId") Long storyId){
        Optional<Story> fetchedStory = storyService.getStoryById(storyId);
        return fetchedStory.isPresent() ? new ResponseEntity(fetchedStory.get(), HttpStatus.OK)
                : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{storyId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity updateStoryById(@RequestBody Story newVersionOfStory, @PathVariable(value = "storyId") Long storyId){
        ResponseEntity responseEntity = null;
        try {
            Optional<Story> fetchedStory = storyService.updateStoryById(storyId, newVersionOfStory);
            responseEntity = fetchedStory.isPresent() ? new ResponseEntity(fetchedStory.get(), HttpStatus.OK)
                    : new ResponseEntity(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            responseEntity = new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }finally {
            return responseEntity;
        }
    }

    private Throwable getRootThrowable(Throwable e) {
        while (e.getCause() != null){
            e = e.getCause();
        }
        return e;
    }
}
