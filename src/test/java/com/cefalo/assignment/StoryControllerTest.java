package com.cefalo.assignment;

import com.cefalo.assignment.controller.StoryController;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.business.StoryService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StoryController.class)
public class StoryControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private StoryService storyService;
    @Autowired Gson gson;

    @Test
    public void postStoryObjectTest()throws Exception{

        Story demoStory = new Story("foo", "MR foo", "Date");
        mvc
                .perform(post("/api/stories")
                        .content(gson.toJson(demoStory))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(storyService, times(1)).saveNewStoryObject(demoStory);
    }
}
