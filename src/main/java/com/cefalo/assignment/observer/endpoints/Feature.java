package com.cefalo.assignment.observer.endpoints;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Feature implements Serializable {
    private Boolean enabled;
}