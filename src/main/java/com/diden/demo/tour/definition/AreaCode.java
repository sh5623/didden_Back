package com.diden.demo.tour.definition;

import lombok.Getter;

@Getter
public enum AreaCode {
  SEOUL(1, "서울"),
  INCHEON(2, "인천"),
  DAEJEON(3, "대전"),
  DAEGU(4, "대구"),
  GWANGJU(5, "광주"),
  BUSAN(6, "부산"),
  ULSAN(7, "울산"),
  SEJONG(8, "세종특별자치시"),
  GYEONGGI(31, "경기도"),
  GANGWON(32, "강원도"),
  CHUNGBUK(33, "충청북도"),
  CHUNGNAM(34, "충청남도"),
  GYEONGBUK(35, "경상북도"),
  GYEONGNAM(36, "경상남도"),
  JEONBUK(37, "전라북도"),
  JEONNAM(38, "전라남도"),
  JEJU(39, "제주도"),
  ;
  private final Integer code;
  private final String name;

  AreaCode(Integer code, String name) {
    this.code = code;
    this.name = name;
  }
}
