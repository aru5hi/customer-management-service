package com.example.restApi.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseModel {
    private String type;
    private String title;
    private int status;
    private String details;
    private String instance;
    private Instant timestamp;
    private List<FieldErrorsModel> errors;

    public ErrorResponseModel(int status, String title, String details, String instance){
        this.status = status;
        this.title = title;
        this.details =details;
        this.instance = instance;
        this.timestamp = Instant.now();
        this.type = "https://localhost:8443/errors/"+title.toLowerCase().replace(" ", "-");
    }
}
