package com.khata.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceAlreadyExistsException extends RuntimeException {

    private String resourceName;
    private Object fieldValue;

    public ResourceAlreadyExistsException(String resourceName, Object fieldValue) {
        super(String.format("%s already exists: %s", resourceName, fieldValue));
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
    }
}
