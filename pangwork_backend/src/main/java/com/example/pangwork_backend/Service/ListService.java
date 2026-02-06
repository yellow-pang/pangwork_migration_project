package com.example.pangwork_backend.Service;

import java.util.Map;

public interface ListService {
    public Map<String,Object> getMainWorks(Map<String, Object> params);
    public Map<String,Object> getWorks(Map<String, Object> params);
    public Map<String,Object> getDetail(Map<String, Object> params);

}
