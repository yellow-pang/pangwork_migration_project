package com.example.pangwork_backend.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.pangwork_backend.DTO.DetailWorkout;
import com.example.pangwork_backend.DTO.MainWorkout;

@Mapper
public interface ListMapper {
    public List<MainWorkout> selectMainWorkout(
        @Param("params") Map<String, Object> params
    );

    public int selectListCount(
        @Param("params") Map<String, Object> params
    );

    public List<DetailWorkout> selectLists(
        @Param("params") Map<String, Object> params
    );

    public List<DetailWorkout> selectListDetail(
        @Param("params") Map<String, Object> params
    );
}
