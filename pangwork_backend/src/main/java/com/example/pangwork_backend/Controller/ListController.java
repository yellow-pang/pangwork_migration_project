package com.example.pangwork_backend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.pangwork_backend.Service.ListService;

@Controller
@RequestMapping("/api/list")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @RequestMapping(value = "/mainWorks", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getMainWorks(
        @RequestBody Map<String, Object> params
    ) {
        Map<String, Object> result = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userId = user.getUsername();
        params.put("userId", userId);
        result = listService.getMainWorks(params);

        return result;
    }

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getLists(
        @RequestBody Map<String, Object> params
    ) {
        Map<String, Object> result = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userId = user.getUsername();
        params.put("userId", userId);
        result = listService.getWorks(params);
        return result;
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getListDetail(
        @RequestBody Map<String, Object> params
    ) {
        Map<String, Object> result = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userId = user.getUsername();
        params.put("userId", userId);
        result = listService.getDetail(params);
        return result;
    }
}
