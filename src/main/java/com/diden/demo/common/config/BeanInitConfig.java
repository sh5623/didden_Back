package com.diden.demo.common.config;

import com.diden.demo.common.adepter.*;
import com.diden.demo.domain.user.adepter.AppleSocialAdepterImpl;
import com.diden.demo.domain.user.adepter.KakaoSocialAdepterImpl;
import com.diden.demo.domain.user.adepter.NaverSocialAdepterImpl;
import com.diden.demo.domain.user.adepter.SocialAdepter;
import com.diden.demo.domain.user.enums.AccountTypeEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class BeanInitConfig {
  private final Map<AccountTypeEnum, SocialAdepter> socialAdepterMap = new HashMap<>();
  private final Map<AccountTypeEnum, LoginLogoutAdepter> loginLogoutAdepterMap = new HashMap<>();

  @Bean
  public Map<AccountTypeEnum, LoginLogoutAdepter> loginLogoutAdepterMap() {
    this.loginLogoutAdepterMap.put(AccountTypeEnum.NAVER, new LoginLogoutNaver());
    this.loginLogoutAdepterMap.put(AccountTypeEnum.KAKAO, new LoginLogoutKakao());
    this.loginLogoutAdepterMap.put(AccountTypeEnum.APPLE, new LoginLogoutApple());
    this.loginLogoutAdepterMap.put(AccountTypeEnum.DEFAULT, new LoginLogoutDefault());
    return loginLogoutAdepterMap;
  }

  @Bean
  public Map<AccountTypeEnum, SocialAdepter> socialAdepterMap() {
    this.socialAdepterMap.put(AccountTypeEnum.NAVER, new NaverSocialAdepterImpl());
    this.socialAdepterMap.put(AccountTypeEnum.KAKAO, new KakaoSocialAdepterImpl());
    this.socialAdepterMap.put(AccountTypeEnum.APPLE, new AppleSocialAdepterImpl());
    return socialAdepterMap;
  }
}