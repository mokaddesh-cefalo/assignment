package com.cefalo.assignment.model.business;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Data
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "story")
public class StoryProperties {
    private int articlePerPage;
    private int defaultPaginationPageNumber;
    private int deleteNotFoundStatusCode;
    private int deleteNotAuthorizedStatusCode;
    private int deleteOnSuccess;
    private String defaultPaginationColumnName;
    private String replaceFieldsOnUpdate;
    private String fieldsNameToUseInPagination;

    private HashSet<String> setOfReplaceFieldsOnUpdate;
    private HashSet<String> setOfFieldsNameToUseInPagination;

    public StoryProperties(){
    }

    void init(){
    }

    public HashSet<String> getSetOfReplaceFieldsOnUpdate(){
        if(setOfReplaceFieldsOnUpdate == null){
            setOfReplaceFieldsOnUpdate = new HashSet<>();
            setOfReplaceFieldsOnUpdate.addAll(
                    makeStringToStringList(replaceFieldsOnUpdate, ",")
            );
        }

        return setOfReplaceFieldsOnUpdate;
    }

    public HashSet<String> getSetOfFieldsNameToUseInPagination(){

        if(setOfFieldsNameToUseInPagination == null){
            setOfFieldsNameToUseInPagination = new HashSet<>();
            setOfFieldsNameToUseInPagination.addAll(
                    makeStringToStringList(fieldsNameToUseInPagination, ",")
            );
        }
        return setOfFieldsNameToUseInPagination;
    }

    private List<String> makeStringToStringList(String mainString, String seperator){
        return Arrays.stream(mainString.split(seperator))
                .collect(Collectors.toList());
    }
}
