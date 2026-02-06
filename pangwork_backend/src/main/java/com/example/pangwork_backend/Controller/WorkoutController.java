package com.example.pangwork_backend.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.pangwork_backend.DTO.DetailWorkout;
import com.example.pangwork_backend.Service.WorkoutService;

/**
 * 운동 관련 컨트롤러
 */
@Controller
@RequestMapping("/work")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> test() {
        Map<String, Object> map = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        System.out.println(user.toString());
        return map;
    }

    @RequestMapping(value = "/works", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getMainWorkouts(
        @RequestBody Map<String, Object> params
    ) {
        Map<String, Object> result = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userId = user.getUsername();
        
        params.put("userId", userId);
        result = workoutService.getMainworkouts(params);
        return result;
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getDetailWorkouts(
        @RequestBody Map<String, Object> params
    ) {
        Map<String, Object> result = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userId = user.getUsername();
        params.put("userId", userId);
        result = workoutService.getWorkDetail(params);

        return result;
    }

    @RequestMapping(value = "/detail/deploy", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> setDetailWorkouts(
        @RequestBody Map<String, Object> params
    ) {
        Map<String, Object> result = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userId = user.getUsername();
        params.put("userId", userId);

        // int workId=Integer.parseInt((String)params.get("workId"));
        int workId=(Integer)params.get("workId");
        int workSetId=Integer.parseInt((String)params.get("workSetId"));
        String memo=(String)params.get("memo");
			
        // 리스트 쪼개기
        List<LinkedHashMap<String, Object>> warmList = (List<LinkedHashMap<String, Object>>) params.get("warmup");
        List<LinkedHashMap<String, Object>> bonList = (List<LinkedHashMap<String, Object>>) params.get("bon");
        List<LinkedHashMap<String, Object>> nextList = (List<LinkedHashMap<String, Object>>) params.get("next");

        // 빈 리스트 추가
        List<DetailWorkout> insertWorkList = new ArrayList<>();
        List<DetailWorkout> updateWorkList = new ArrayList<>();

        // 웜업 세트 세팅
        for(LinkedHashMap<String, Object> item : warmList){
            DetailWorkout tempDetailWorkout = new DetailWorkout();
            tempDetailWorkout.setWorkId(workId);
            tempDetailWorkout.setWorkSetId(workSetId);
            tempDetailWorkout.setUserId(userId);
            tempDetailWorkout.setWorkDetailId((String)item.get("id"));
            tempDetailWorkout.setWorkDetailName((String)item.get("name"));
            tempDetailWorkout.setWorkDetailCount((String)item.get("count"));
            tempDetailWorkout.setWorkDetailCategory("W");
            if(tempDetailWorkout.getWorkDetailId().equals("0")){
                insertWorkList.add(tempDetailWorkout);
            }else{
                updateWorkList.add(tempDetailWorkout);
            }
            
        }

        // 본 세트 세팅
        for(LinkedHashMap<String, Object> item : bonList){
            DetailWorkout tempDetailWorkout = new DetailWorkout();
            tempDetailWorkout.setWorkId(workId);
            tempDetailWorkout.setWorkSetId(workSetId);
            tempDetailWorkout.setUserId(userId);
            tempDetailWorkout.setWorkDetailId((String)item.get("id"));
            tempDetailWorkout.setWorkDetailName((String)item.get("name"));
            tempDetailWorkout.setWorkDetailCount((String)item.get("count"));
            tempDetailWorkout.setWorkDetailCategory("B");
            if(tempDetailWorkout.getWorkDetailId().equals("0")){
                insertWorkList.add(tempDetailWorkout);
            }else{
                updateWorkList.add(tempDetailWorkout);
            }
        }

        // 다음 목표 세트 세팅
        for(LinkedHashMap<String, Object> item : nextList){
            DetailWorkout tempDetailWorkout = new DetailWorkout();
            tempDetailWorkout.setWorkId(workId);
            tempDetailWorkout.setWorkSetId(workSetId);
            tempDetailWorkout.setUserId(userId);
            tempDetailWorkout.setWorkDetailId((String)item.get("id"));
            tempDetailWorkout.setWorkDetailName((String)item.get("name"));
            tempDetailWorkout.setWorkDetailCount((String)item.get("count"));
            tempDetailWorkout.setWorkDetailCategory("N");
            tempDetailWorkout.setMemo(memo);
            if(tempDetailWorkout.getWorkDetailId().equals("0")){
                insertWorkList.add(tempDetailWorkout);
            }else{
                updateWorkList.add(tempDetailWorkout);
            }
        }

        System.out.println("새로 추가 목록" + insertWorkList);
        System.out.println("업데이트 목록" +updateWorkList);

        result = workoutService.setWorkDetail(insertWorkList, updateWorkList);


        return result;
    }
}
