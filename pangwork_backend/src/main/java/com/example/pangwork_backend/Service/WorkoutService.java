package com.example.pangwork_backend.Service;

import java.util.List;
import java.util.Map;

import com.example.pangwork_backend.DTO.DetailWorkout;

public interface WorkoutService {
    public Map<String, Object> getMainworkouts(
        Map<String, Object> params
    );

    public Map<String, Object> getWorkDetail(
        Map<String, Object> params
    );

    public Map<String, Object> setWorkDetail(
        List<DetailWorkout> insertList
        ,List<DetailWorkout> updateList
    );
}
