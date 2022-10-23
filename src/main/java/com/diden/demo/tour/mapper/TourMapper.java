package com.diden.demo.tour.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TourMapper {
  List<Map<String, Object>> tourInfoList(Map<String, Object> tourInfoParam);

  void tourInfoInsert(Map<String, Object> tourInfoParam);

  void tourInfoUpdate(Map<String, Object> tourInfoParam);

  void tourInfoDelete();
}
