package com.diden.tour.service.impl;

import com.diden.tour.mapper.TourMapper;
import com.diden.tour.service.TourService;
import com.diden.user.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TourServiceImpl implements TourService {

    @Autowired
    TourMapper tourMapper;

    @Override
    public List<Map<String, Object>> tourInfoList(Map<String, Object> tourInfoParam){
        System.out.println("tourInfoParam="+tourInfoParam);
        return tourMapper.tourInfoList(tourInfoParam);
    }

}
