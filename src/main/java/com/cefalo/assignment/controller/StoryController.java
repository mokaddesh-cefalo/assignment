package com.cefalo.assignment.controller;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.business.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    StoryService storyService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Story postStoryObject(@RequestBody Story story){
        return storyService.postStoryObject(story).orElse(story);
    }

    @GetMapping(consumes = "application/json", produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Story> getAllStoryJson(){
        return storyService.getAllStoryJson();
    }

    private Throwable getRootThrowable(Throwable e) {
        while (e.getCause() != null){
            e = e.getCause();
        }
        return e;
    }
}
