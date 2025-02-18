package com.khata.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String fieldName;
    long longValue;

    public ResourceNotFoundException(String resourceName, String fieldName, long longValue){
        super(String.format("%s not found with %s : %d", resourceName, fieldName, longValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.longValue = longValue;
    }
}