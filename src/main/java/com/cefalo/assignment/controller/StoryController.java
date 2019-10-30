package com.cefalo.assignment.controller;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.business.StoryService;
import com.cefalo.assignment.utils.ExceptionHandlerUtil;
import com.cefalo.assignment.utils.ResponseEntityCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stories")
public class StoryController  {
    private final StoryService storyService;
    private final ExceptionHandlerUtil exceptionHandlerUtil;
    private final ResponseEntityCreation responseEntityCreation;

    @Autowired
    public StoryController(StoryService storyService, ExceptionHandlerUtil exceptionHandlerUtil,
                           ResponseEntityCreation responseEntityCreation){
        this.storyService = storyService;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
        this.responseEntityCreation = responseEntityCreation;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Story> getAllStory(){
        return storyService.getAllStory();
    }

    @GetMapping(value = "/{story-id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getStoryById(@PathVariable(value = "story-id") Long storyId){
        Optional<Story> fetchedStory = storyService.getStoryById(storyId);

        return responseEntityCreation
                .makeResponseEntity(fetchedStory, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/pagination", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Story> getAllStoryByPagination(
            @RequestParam(value = "page-number", defaultValue = "${story.defaultPaginationPageNumber}") Integer pageNumber,
            @RequestParam(value = "column-name", defaultValue = "${story.defaultPaginationColumnName}") String columnName
    ){
        return storyService.findAll(pageNumber, columnName);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?>  postStoryObject(@RequestBody Story story){
        try {
            return responseEntityCreation
                    .makeResponseEntity(storyService.saveNewStoryObject(story), HttpStatus.CREATED);
        }catch (Exception e){
           return responseEntityCreation
            .makeResponseEntity(exceptionHandlerUtil.getRootThrowableMessage(e), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(value = "/{story-id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?>  updateStoryById(@RequestBody Story newVersionOfStory, @PathVariable(value = "story-id") Long storyId){
        try {
            Optional<Story> fetchedStory = storyService.checkAuthorityThenUpdateStoryById(storyId, newVersionOfStory);
            return responseEntityCreation
                    .makeResponseEntity(fetchedStory, HttpStatus.OK, HttpStatus.NOT_FOUND);

        }catch (Exception e){
            return responseEntityCreation
                    .makeResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{story-id}")
    public void deleteStoryById(@PathVariable(value = "story-id") Long storyId, HttpServletResponse response){
        response.setStatus(storyService.checkAuthorityThenDeleteStoryById(storyId));
    }

}
