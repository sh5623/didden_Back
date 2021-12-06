package com.diden.user.service;

import java.util.List;

import com.diden.user.vo.UserVo;

public interface UserService {
    public int userCount(UserVo userVo);

    public List<UserVo> userList();

    public UserVo userInfo(UserVo userVo);

    public void userInsert(UserVo userVo);

    public void userUpdate(UserVo userVo);

    public void userRefTokenUpdate(UserVo userVo);

    public void userDelete(UserVo userVo);

    public UserVo userRefreshTokenInfo(UserVo userVo);
}
