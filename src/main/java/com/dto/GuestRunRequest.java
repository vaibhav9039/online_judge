package com.dto;


import lombok.Data;

@Data
public class GuestRunRequest {
    private String language;
    private String code;
    private String input;
}
