package com.joseph.chatapp.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusDTO<T> {

    private Integer statusCode;
    private String statusDescription;
    private T data;
}