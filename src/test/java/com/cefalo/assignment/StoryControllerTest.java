//package com.cefalo.assignment;
//
//import com.cefalo.assignment.config.AppConfig;
//import com.cefalo.assignment.config.WebSecurityConfig;
//import com.cefalo.assignment.controller.StoryController;
//import com.cefalo.assignment.model.orm.Story;
//import com.cefalo.assignment.service.business.StoryService;
//import com.google.gson.Gson;
//import org.joda.time.DateTime;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockServletContext;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Date;
//
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class StoryControllerTest {
//
//    @Autowired private MockMvc mvc;
//    @MockBean private StoryService storyService;
//    @Autowired Gson gson;
//
//    @Test
//    public void postStoryObjectTest()throws Exception{
//        String dateInString = "05 january 2018";
//        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM yyyy");
//        Date date = DateTime.parse(dateInString, formatter).toDate();
//
//        Story demoStory = new Story("foo", "MR foo", date);
//        mvc
//                .perform(post("/api/stories")
//                        .content(gson.toJson(demoStory))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//
//        verify(storyService, times(1)).saveNewStoryObject(demoStory);
//    }
//}
