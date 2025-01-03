package com.fintech.dto;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ApiResponse {

    public static <T> ResponseEntity<ResponseDto<T>> ok(T data, String message) {
        ResponseDto<T> responseDto = new ResponseDto<>();
        responseDto.setData(data);
        responseDto.setStatusCode("200");
        responseDto.setStatusMessage(message);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
