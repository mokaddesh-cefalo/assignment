package com.cefalo.assignment;

import com.cefalo.assignment.model.orm.Story;

@FunctionalInterface
public interface SortedByStoryField {

    void check(Story A,Story B) throws Exception;
}
