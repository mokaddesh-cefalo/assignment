package com.cefalo.assignment;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.orm.StoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class StoryRepositoryTest {

    @Autowired
    StoryRepository storyRepository;

    @Test
    void contextLoads() {
        Story story = new Story("phase-1", "ME is wrong word", "05 July 2018");
        Story savedStory = storyRepository.save(story);
        assert (savedStory.getId() != null);
    }

}
