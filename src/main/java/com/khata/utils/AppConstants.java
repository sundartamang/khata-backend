package com.khata.utils;

import java.util.List;

public class AppConstants {
    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "10";
    public static final String SORT_DIR = "asc";
    public static final String SORT_BY = "id";

    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static final String ANGULAR_URL = "http://localhost:4200";
    public static final List<String> ALLOWED_CORS_METHODS = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
    public static final List<String> ALLOWED_CORS_HEADERS = List.of("Authorization", "Cache-Control", "Content-Type");
}