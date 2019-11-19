package com.cefalo.assignment.controller;

import com.cefalo.assignment.exception.EntityNotFoundException;
import com.cefalo.assignment.exception.UnAuthorizedRequestException;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.business.StoryService;
import com.cefalo.assignment.utils.ResponseEntityCreation;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stories")
public class StoryController  {

    private static Logger logger = LogManager.getLogger("com");
    private final StoryService storyService;
    private final ResponseEntityCreation responseEntityCreation;

    @Autowired
    public StoryController(StoryService storyService,
                           ResponseEntityCreation responseEntityCreation){
        this.storyService = storyService;
        this.responseEntityCreation = responseEntityCreation;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{story-id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Story getStoryById
            (@PathVariable(value = "story-id") Long storyId) throws EntityNotFoundException {

        return storyService.getStoryById(storyId);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Story> getAllStoryByPagination(
            @RequestParam(value = "page", defaultValue = "${story.defaultPaginationPageNumber}") Integer pageNumber,
            @RequestParam(value = "sort", defaultValue = "${story.defaultPaginationColumnName}") String sortByColumnName,
            @RequestParam(value = "limit", defaultValue = "${story.articlePerPage}") Integer limit
    ){
        return storyService.findAllForPagination(pageNumber, limit, sortByColumnName);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Story  postStoryObject(@RequestBody @Valid Story story)
            throws InvalidFormatException {

            return storyService.saveNewStoryObject(story);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{story-id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Story  patchStoryById(@RequestBody Story newVersionOfStory, @PathVariable(value = "story-id") Long storyId)
            throws UnAuthorizedRequestException, EntityNotFoundException, IllegalAccessException {

        return getResponseEntityForUpdate(newVersionOfStory, storyId, true);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{story-id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Story putStoryById(@RequestBody Story newVersionOfStory, @PathVariable(value = "story-id") Long storyId)
            throws UnAuthorizedRequestException, EntityNotFoundException, IllegalAccessException  {

        return getResponseEntityForUpdate(newVersionOfStory, storyId, false);
    }

    private Story getResponseEntityForUpdate(Story newVersionOfStory, Long storyId, Boolean isPatchUpdate)
            throws UnAuthorizedRequestException, EntityNotFoundException, IllegalAccessException {

            return storyService
                    .checkAuthorityThenUpdateStoryById(storyId, newVersionOfStory, isPatchUpdate);
    }

    @DeleteMapping("/{story-id}")
    public void deleteStoryById(@PathVariable(value = "story-id") Long storyId, HttpServletResponse response){
        response.setStatus(storyService.checkAuthorityThenDeleteStoryById(storyId));
    }

}
