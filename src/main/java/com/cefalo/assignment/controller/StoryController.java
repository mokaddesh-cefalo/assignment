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

    @GetMapping(value = "/{story-id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getStoryById
            (@PathVariable(value = "story-id") Long storyId) throws EntityNotFoundException {

        return responseEntityCreation
                .buildResponseEntity(storyService.getStoryById(storyId), HttpStatus.OK);
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

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?>  postStoryObject(@RequestBody @Valid Story story)
            throws InvalidFormatException {

            return responseEntityCreation
                    .buildResponseEntity(storyService.saveNewStoryObject(story), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{story-id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?>  patchStoryById(@RequestBody Story newVersionOfStory, @PathVariable(value = "story-id") Long storyId)
            throws UnAuthorizedRequestException, EntityNotFoundException, IllegalAccessException {

        return getResponseEntityForUpdate(newVersionOfStory, storyId, true);
    }

    @PutMapping(value = "/{story-id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?>  putStoryById(@RequestBody Story newVersionOfStory, @PathVariable(value = "story-id") Long storyId)
            throws UnAuthorizedRequestException, EntityNotFoundException, IllegalAccessException  {

        return getResponseEntityForUpdate(newVersionOfStory, storyId, false);
    }

    private ResponseEntity<?> getResponseEntityForUpdate(Story newVersionOfStory, Long storyId, Boolean isPatchUpdate)
            throws UnAuthorizedRequestException, EntityNotFoundException, IllegalAccessException {

            Optional<Story> fetchedStory = storyService
                    .checkAuthorityThenUpdateStoryById(storyId, newVersionOfStory, isPatchUpdate);

            return responseEntityCreation
                    .buildResponseEntity(fetchedStory, HttpStatus.OK);

    }

    @DeleteMapping("/{story-id}")
    public void deleteStoryById(@PathVariable(value = "story-id") Long storyId, HttpServletResponse response){
        response.setStatus(storyService.checkAuthorityThenDeleteStoryById(storyId));
    }

}
