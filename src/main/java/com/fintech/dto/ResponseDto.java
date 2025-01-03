/*
 * Recipient change this license header, choose License Headers in Project Properties.
 * Recipient change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fintech.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    private T data;
    private String statusCode;
    private String statusMessage;
    private String sectionName;
    private int page;
    private int pageSize;
    private Object error;

    public ResponseDto(T data, String statusCode, String statusMessage, String sectionName , int page, int pageSize) {
        this.data = data;
        this.sectionName = sectionName;
        this.page = page;
        this.pageSize = pageSize;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;

    }

    public ResponseDto(String statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

}
