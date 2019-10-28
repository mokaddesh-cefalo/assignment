package com.cefalo.assignment.controller;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.business.StoryService;
import com.cefalo.assignment.service.orm.StoryRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @ApiOperation(response = Story.class, value = "Find All the story",
    notes = "Provide 'Accept' headers 'application/json' for json response, &nbsp 'application/xml' for xml type response")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Story> getAllStory(){
        return storyService.getAllStory();
    }

    /**using @ApiParam(value = "Id value for Story you need to retrieve", required = true) throws null pointer exception*/
    @ApiOperation(response = Story.class, value = "Find a story by id",
            notes = "Provide 'Accept' headers application/json for json response, application/xml for xml type response")
    @GetMapping(value = "/{storyId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getStoryById(@PathVariable(value = "storyId") Long storyId){
        Optional<Story> fetchedStory = storyService.getStoryById(storyId);
        return fetchedStory.isPresent() ? new ResponseEntity(fetchedStory.get(), HttpStatus.OK)
                : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(response = Story.class, value = "Find story through pagination",
            notes = "Provide 'Accept' headers application/json for json response, application/xml for xml type response <br>" +
                    "'pagenumber' for number of the page<br>" +
                    "'columnName' for name of Story table column which will be use for sorting")
    @GetMapping(value = "/pagination", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Story> getAllStoryByPagination(
            @RequestParam(value = "pagenumber", defaultValue = "${story.defaultPaginationPageNumber}") Integer pageNumber,
            @RequestParam(value = "columnName", defaultValue = "${story.defaultPaginationColumnName}") String columnName
    ){
        return storyService.findAll(pageNumber, columnName);
    }

    @ApiOperation(response = Story.class, value = "Create a new story attached in request body",
            notes = "Provide 'Content-Type' headers 'application/json' for story in json format<br> " +
                    "'application/xml' for xml format <br>',")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity postStoryObject(@RequestBody Story story){
        ResponseEntity responseEntity = null;

        try {
            responseEntity = new ResponseEntity<Story>(storyService.postStoryObject(story), HttpStatus.CREATED);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(getRootThrowableMessage(e), HttpStatus.UNPROCESSABLE_ENTITY);
        }finally {
            return responseEntity;
        }
    }

    @ApiOperation(response = Story.class, value = "Update a story attached in request body and id will be fetched from url<br>" +
            "Only creator of the story can update it",
            notes = "Provide 'Content-Type' headers 'application/json' for story in json format<br> " +
                    "'application/xml' for xml format <br>'")
    @PostMapping(value = "/{storyId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity updateStoryById(@RequestBody Story newVersionOfStory, @PathVariable(value = "storyId") Long storyId){
        ResponseEntity responseEntity = null;
        try {
            Optional<Story> fetchedStory = storyService.updateStoryById(storyId, newVersionOfStory);
            responseEntity = fetchedStory.isPresent() ? new ResponseEntity<Story>(fetchedStory.get(), HttpStatus.OK)
                    : new ResponseEntity(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }finally {
            return responseEntity;
        }
    }

    @ApiOperation(value = "Delete a story using ID.Only creator of the story can Delete it")
    @DeleteMapping("/{storyId}")
    public void deleteStoryById(@PathVariable(value = "storyId") Long storyId, HttpServletResponse response){
        response.setStatus((int)storyService.deleteStoryById(storyId));
    }

    private String getRootThrowableMessage(Throwable e) {
        while (e.getCause() != null){
            e = e.getCause();
        }
        return e.getMessage();
    }
}
