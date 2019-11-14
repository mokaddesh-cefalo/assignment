//package com.cefalo.assignment;
//import com.cefalo.assignment.model.orm.Story;
//import com.cefalo.assignment.repositories.StoryRepository;
//import org.assertj.core.error.ShouldBeAfterYear;
//import org.joda.time.DateTime;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//class StoryRepositoryTest {
//
//    @Autowired
//    StoryRepository storyRepository;
//
//    @Test
//    void saveNewStory() {
//
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken("fish", "pass");
//        System.out.println(usernamePasswordAuthenticationToken);
//
//        String dateInString = "05 january 2018";
//        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM yyyy");
//        Date date = DateTime.parse(dateInString, formatter).toDate();
//
//        Story story = new Story("phase-1", "ME is wrong word", date);
//        Story savedStory = storyRepository.save(story);
//        savedStory.setCreatorName();
//        System.out.println(savedStory.getCreatorName());
//
//        assertNotNull(savedStory.getId());
//        assertEquals(storyRepository.findById(savedStory.getId()).get().getBody(),
//                story.getBody());
//    }
//
//}
