package com.example.pangwork_backend.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangwork_backend.DTO.DetailWorkout;
import com.example.pangwork_backend.DTO.MainWorkout;
import com.example.pangwork_backend.Mapper.WorkoutsMapper;
import com.example.pangwork_backend.Service.WorkoutService;

@Service
public class WorkoutServiceImpl implements WorkoutService{
  
  private final WorkoutsMapper workoutsMapper;

  public WorkoutServiceImpl(WorkoutsMapper workoutsMapper) {
		this.workoutsMapper = workoutsMapper;
  }

  @Override
    public Map<String, Object> getMainworkouts(
        Map<String, Object> params
        ){
        Map<String, Object> map = new HashMap<>();
        try {
			List<MainWorkout> data = workoutsMapper.selectMainWorkouts(params);
	
			map.put("status", "200");
			map.put("error", "");
			map.put("data", data);
		} catch(Exception ex) {
			ex.printStackTrace();

			map.put("status", "800");
			map.put("error", "DB 조회 오류");
			map.put("data", "");
		}
        return map;
    }

	@Override
    public Map<String, Object> getWorkDetail(
        Map<String, Object> params
        ){
        Map<String, Object> map = new HashMap<>();
        try {
			List<DetailWorkout> data = workoutsMapper.selectDetailWorkout(params);
			System.out.println(data);
			System.out.println(params);
			map.put("status", "200");
			map.put("error", "");
			map.put("data", data);
		} catch(Exception ex) {
			ex.printStackTrace();

			map.put("status", "800");
			map.put("error", "DB 조회 오류");
			map.put("data", "");
		}
        return map;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> setWorkDetail(
		List<DetailWorkout> insertList
        ,List<DetailWorkout> updateList
        ){
        Map<String, Object> map = new HashMap<>();
		try {
			if(insertList.size() > 0){
				int data = workoutsMapper.insertDetailWorkout(insertList);
			}
			if(updateList.size() > 0){
				for(DetailWorkout i:updateList){
					int data = workoutsMapper.updateDetailWorkout(i);
				}
			}
			map.put("status", "200");
			map.put("error", "");
			map.put("data", "");
		} catch(Exception ex) {
			ex.printStackTrace();

			map.put("status", "800");
			map.put("error", "DB 조회 오류");
			map.put("data", "");
		}
        return map;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, Object> addWorkout(
		Map<String, Object> params
		){
		Map<String, Object> map = new HashMap<>();
		try {
			int duplicateCount = workoutsMapper.selectWorkoutNameCount(params);
			if (duplicateCount > 0) {
				map.put("status", "409");
				map.put("error", "이미 등록된 운동 이름입니다.");
				map.put("data", "");
				return map;
			}

			int data = workoutsMapper.insertWorkout(params);
			Object workId = params.get("workId");
			MainWorkout workout = workoutsMapper.selectWorkoutById(params);
			map.put("status", "200");
			map.put("error", "");
			map.put("data", workout);
			map.put("workId", workId);
			map.put("message", "운동이 추가되었습니다.");
		} catch(Exception ex) {
			ex.printStackTrace();

			map.put("status", "800");
			map.put("error", "DB 처리 오류");
			map.put("data", "");
		}
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, Object> deleteWorkout(
		Map<String, Object> params
		){
		Map<String, Object> map = new HashMap<>();
		try {
			int detailDeleteCount = workoutsMapper.deleteWorkDetailsByWorkId(params);
			int data = workoutsMapper.deleteWorkout(params);
			if (data > 0) {
				map.put("status", "200");
				map.put("error", "");
				map.put("data", data);
				map.put("detailDeleteCount", detailDeleteCount);
				map.put("message", "운동이 삭제되었습니다.");
			} else {
				map.put("status", "404");
				map.put("error", "삭제할 운동을 찾을 수 없습니다.");
				map.put("data", data);
				map.put("detailDeleteCount", detailDeleteCount);
			}
		} catch(Exception ex) {
			ex.printStackTrace();

			map.put("status", "800");
			map.put("error", "DB 처리 오류");
			map.put("data", "");
		}
		return map;
	}

}
