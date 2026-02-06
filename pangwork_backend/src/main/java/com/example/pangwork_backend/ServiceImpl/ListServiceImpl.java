package com.example.pangwork_backend.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.pangwork_backend.DTO.DetailWorkout;
import com.example.pangwork_backend.DTO.MainWorkout;
import com.example.pangwork_backend.Mapper.ListMapper;
import com.example.pangwork_backend.Service.ListService;

@Service
public class ListServiceImpl implements ListService{

  private final ListMapper listMapper;
  
    public ListServiceImpl(ListMapper listMapper) {
    this.listMapper = listMapper;
  }
  
  @Override
    public Map<String,Object> getMainWorks(Map<String, Object> params){
        Map<String,Object> result = new HashMap<>();
        try {
            List<MainWorkout> data = listMapper.selectMainWorkout(params);
            result.put("status", "200");
			result.put("error", "");
			result.put("data", data);
        } catch(Exception ex) {
			ex.printStackTrace();

			result.put("status", "800");
			result.put("error", "DB 조회 오류");
			result.put("data", "");
		}
        return result;
    }

    @Override
    public Map<String,Object> getWorks(Map<String, Object> params){
        Map<String,Object> result = new HashMap<>();
        try {
            int totalCount = listMapper.selectListCount(params);
            List<DetailWorkout> data = listMapper.selectLists(params);
            result.put("status", "200");
			result.put("error", "");
			result.put("data", data);
			result.put("totalCount", totalCount);
        } catch(Exception ex) {
			ex.printStackTrace();

			result.put("status", "800");
			result.put("error", "DB 조회 오류");
			result.put("data", "");
            result.put("totalCount", 0);
		}
        return result;
    }

    @Override
    public Map<String,Object> getDetail(Map<String, Object> params){
        Map<String,Object> result = new HashMap<>();
        try {
            List<DetailWorkout> data = listMapper.selectListDetail(params);
            System.out.println(params);
            result.put("status", "200");
			result.put("error", "");
			result.put("data", data);
        } catch(Exception ex) {
			ex.printStackTrace();

			result.put("status", "800");
			result.put("error", "DB 조회 오류");
			result.put("data", "");
            result.put("totalCount", 0);
		}
        return result;
    }
}
