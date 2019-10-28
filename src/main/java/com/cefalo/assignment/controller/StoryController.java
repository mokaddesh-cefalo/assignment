package com.cefalo.assignment.controller;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.business.StoryService;
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

    @Autowired
    StoryService storyService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Story> getAllStory(){
        return storyService.getAllStory();
    }

    @GetMapping(value = "/{storyId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getStoryById(@PathVariable(value = "storyId") Long storyId){
        Optional<Story> fetchedStory = storyService.getStoryById(storyId);

        return fetchedStory.isPresent() ? makeResponseEntity(fetchedStory.get(), HttpStatus.OK)
                : makeResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/pagination", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Story> getAllStoryByPagination(
            @RequestParam(value = "pagenumber", defaultValue = "${story.defaultPaginationPageNumber}") Integer pageNumber,
            @RequestParam(value = "columnName", defaultValue = "${story.defaultPaginationColumnName}") String columnName
    ){
        return storyService.findAll(pageNumber, columnName);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity postStoryObject(@RequestBody Story story){
        try {
            return makeResponseEntity(storyService.saveNewStoryObject(story), HttpStatus.CREATED);
        }catch (Exception e){
           return makeResponseEntity(getRootThrowableMessage(e), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(value = "/{storyId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity updateStoryById(@RequestBody Story newVersionOfStory, @PathVariable(value = "storyId") Long storyId){
        try {
            Optional<Story> fetchedStory = storyService.checkAuthorityThenUpdateStoryById(storyId, newVersionOfStory);
            return fetchedStory.isPresent() ? makeResponseEntity(fetchedStory.get(), HttpStatus.OK)
                    : makeResponseEntity(null, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return makeResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{storyId}")
    public void deleteStoryById(@PathVariable(value = "storyId") Long storyId, HttpServletResponse response){
        response.setStatus(storyService.checkAuthorityThenDeleteStoryById(storyId));
    }

    private String getRootThrowableMessage(Throwable e) {
        while (e.getCause() != null){
            e = e.getCause();
        }
        return e.getMessage();
    }

    public <T> ResponseEntity makeResponseEntity(T t, HttpStatus httpStatus){
        return new ResponseEntity(t, httpStatus);
    }
}
