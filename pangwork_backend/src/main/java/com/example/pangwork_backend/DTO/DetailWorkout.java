package com.example.pangwork_backend.DTO;

import lombok.Data;

@Data
public class DetailWorkout {
    private int workId;
    private String workName;
    private String userId;
    private String workDetailId;
    private String workDetailName;
    private int workSetId;
    private String workDetailCount;
    private String workDetailCategory;
    private String createDate;
    private String editDate;
    private String memo;  
}
