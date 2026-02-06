package com.example.pangwork_backend.DTO;

import lombok.Data;

@Data
public class MainWorkout {
    private int workId;
    private String workName;
    private String userId;
    private String createDate;
    private String editDate;
}
