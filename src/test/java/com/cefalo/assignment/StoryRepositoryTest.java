package com.cefalo.assignment;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.entities.StoryRepository;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class StoryRepositoryTest {

    @Autowired
    StoryRepository storyRepository;

    @Test
    void contextLoads() {
        String dateInString = "05 january 2018";
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM yyyy");
        Date date = DateTime.parse(dateInString, formatter).toDate();

        Story story = new Story("phase-1", "ME is wrong word", date);
        Story savedStory = storyRepository.save(story);
        assert (savedStory.getId() != null);
    }

}
