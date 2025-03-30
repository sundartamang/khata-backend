package com.khata.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * Custom exception class that is thrown when a resource is not found.
 * This exception provides additional information such as the resource name,
 * the field name that was used to search for the resource, and either a long
 * or string value representing the field value that was not found.
 * <p>
 * The exception message is constructed based on the resource name, field name,
 * and the corresponding value (either a long or string) that was not found.
 */
@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private long longValue;
    private String stringValue;

    /**
     * Constructor to create a {@link ResourceNotFoundException} with a long field value.
     *
     * @param resourceName the name of the resource that was not found
     * @param fieldName the name of the field used for searching the resource
     * @param longValue the long value of the field that was not found
     */
    public ResourceNotFoundException(String resourceName, String fieldName, long longValue){
        super(String.format("%s not found with %s : %d", resourceName, fieldName, longValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.longValue = longValue;
    }

    /**
     * Constructor to create a {@link ResourceNotFoundException} with a string field value.
     *
     * @param resourceName the name of the resource that was not found
     * @param fieldName the name of the field used for searching the resource
     * @param stringValue the string value of the field that was not found
     */
    public ResourceNotFoundException(String resourceName, String fieldName, String stringValue){
        super(String.format("%s not found with %s : %s", resourceName, fieldName, stringValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.stringValue = stringValue;
    }
}