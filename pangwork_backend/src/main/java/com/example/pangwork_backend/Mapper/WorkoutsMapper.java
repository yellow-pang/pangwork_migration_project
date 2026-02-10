package com.example.pangwork_backend.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.pangwork_backend.DTO.DetailWorkout;
import com.example.pangwork_backend.DTO.MainWorkout;

@Mapper
public interface WorkoutsMapper {
    public List<MainWorkout> selectMainWorkouts(
		@Param("params") Map<String, Object> params
	);

	public List<DetailWorkout> selectDetailWorkout(
		@Param("params") Map<String, Object> params
	);

	public int insertDetailWorkout(
		@Param("params") List<DetailWorkout> detailWorkout
	);

	public int updateDetailWorkout(
		DetailWorkout detailWorkout
	);

	public MainWorkout selectWorkoutById(
		@Param("params") Map<String, Object> params
	);

	public int selectWorkoutNameCount(
		@Param("params") Map<String, Object> params
	);

	public int insertWorkout(
		@Param("params") Map<String, Object> params
	);

	public int deleteWorkDetailsByWorkId(
		@Param("params") Map<String, Object> params
	);

	public int deleteWorkout(
		@Param("params") Map<String, Object> params
	);
	
}
