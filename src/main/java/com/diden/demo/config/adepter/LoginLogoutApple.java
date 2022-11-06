package com.diden.demo.config.adepter;

import com.diden.demo.error.exception.SocialProcessException;

import static com.diden.demo.utils.AccountTypeEnum.APPLE;

public class LoginLogoutApple implements LoginLogoutAdepter {
  @Override
  public boolean supports(String handler) {
    return (APPLE.getAccountType().equals(handler));
  }

  @Override
  public boolean loginProcess(String authorization) {
    throw new SocialProcessException("애플 로그인은 현재 구현되지 않았습니다.");
  }

  @Override
  public boolean logoutProcess(String authorization) {
    throw new SocialProcessException("애플 로그아웃은 현재 구현되지 않았습니다.");
  }

  @Override
  public String findUserEmail(String authorization) {
    return null;
  }
}