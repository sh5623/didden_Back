package com.diden.user.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.diden.demo.ParsingJSONFromURL;
import com.diden.user.service.UserService;
import com.diden.user.vo.UserVo;
import com.google.gson.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserApiController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/user/test")
    public String test() throws IOException {
        ParsingJSONFromURL parsingJSONFromURL = new ParsingJSONFromURL();
        return parsingJSONFromURL.getParsingJSONFromURL(
                "https://api.odcloud.kr/api/15003416/v1/uddi:a635e6c7-82cf-4714-b002-c7cf4cb20121_201609071527?page=1&perPage=10&serviceKey=96EIT1koaTBt2OfbhSFR9PyKGOKS%2FAMqgeugwN1XT2QwjnE97ZiG1uszeNCPJquN2y2XIYC8GX8BlAcpvUcusw%3D%3D");
    }

    @GetMapping(value = "/user/list")
    public String userList() {
        List<UserVo> userVoList = userService.userList();
        JsonObject userJsonList = new JsonObject();

        for (int i = 0; i < userVoList.size(); i++) {
            // Map<String, UserVo> map = userVo.get(i);
            JsonObject userJson = new JsonObject();
            UserVo userVo = (UserVo) userVoList.get(i);
            userJson.addProperty("userId", userVo.getUserId());
            userJson.addProperty("userName", userVo.getUserName());
            userJson.addProperty("userPassword", userVo.getUserPassword());
            userJson.addProperty("userNickname", userVo.getUserNickname());
            userJson.addProperty("userBirthday", userVo.getUserBirthday());
            userJson.addProperty("userGender", userVo.getUserGender());
            userJson.addProperty("userEmail", userVo.getUserEmail());
            userJson.addProperty("userPhoneNumber", userVo.getUserPhoneNumber());
            userJson.addProperty("userCreateDate", userVo.getUserCreateDate());
            userJson.addProperty("userUpdateDate", userVo.getUserUpdateDate());
            userJson.addProperty("userPrivacyConsent", userVo.getUserPrivacyConsent());
            userJsonList.add("user" + i, userJson);
        }

        return userJsonList.toString();
    }

    // Exception 어노테이션.
    // Json Exception 공통.
    // View Exception 공통.
    @PostMapping(value = "/user/login")
    public ResponseEntity<String> userData(@RequestBody(required = false) UserVo userVo, HttpServletRequest request,
            HttpSession session) throws Exception {
        try {
            JsonObject userResult = new JsonObject();
            log.info("Session Check ============================== {}", request.getSession());

            if (Objects.isNull(userVo)) {
                throw new Exception("파라미터 null");
            }

            UserVo userVoData = userService.userInfo(userVo);
            if (Objects.isNull(userVoData)) {
                return new ResponseEntity<>(errorMethod("login", null), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (Objects.toString(userVo.getUserId(), "").equals(userVoData.getUserId())) {
                if (Objects.toString(userVo.getUserPassword(), "").equals(userVoData.getUserPassword())) {
                    userResult.addProperty("result", true);
                    // session = request.getxSession();
                    session.setAttribute("sessionId", userVoData);
                    session.setMaxInactiveInterval(-1);
                    return new ResponseEntity<>(userResult.toString(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(errorMethod("login", null), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(errorMethod("login", null), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(errorMethod("", e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/user/logout")
    public ResponseEntity<String> userLogout(@RequestBody(required = false) UserVo userVo, HttpServletRequest request)
            throws Exception {
        try {
            JsonObject userResult = new JsonObject();
            log.info("Session Check ============================== {}", request.getSession());

            if (request.getSession() != null) {
                HttpSession session = request.getSession();
                session.invalidate();

                userResult.addProperty("result", true);
                return new ResponseEntity<>(userResult.toString(), HttpStatus.OK);
            } else {
                throw new Exception("잘못된 접근.");
            }

        } catch (Exception e) {
            return new ResponseEntity<>(errorMethod("", e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/user")
    public ResponseEntity<String> userInsert(@RequestBody(required = false) UserVo userVo) {
        JsonObject userResult = new JsonObject();
        try {
            if (Objects.isNull(userVo)) {
                throw new Exception("파라미터 null");
            }

            UserVo userCheck = new UserVo();
            userCheck.setUserId(userVo.getUserId());
            userCheck.setUserPassword(userVo.getUserPassword());

            userCheck = userService.userInfo(userCheck);
            if (Objects.isNull(userCheck)) {
                userService.userInsert(userVo);
                userResult.addProperty("result", true);
                userResult.addProperty("put", "insert");
            } else {
                userService.userUpdate(userVo);
                userResult.addProperty("result", true);
                userResult.addProperty("put", "update");
            }

        } catch (Exception e) {
            // TODO: handle exception
            userResult.addProperty("result", false);
            userResult.addProperty("error", e.getMessage());
            return new ResponseEntity<>(userResult.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userResult.toString(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/user")
    public ResponseEntity<String> userDelete(@RequestBody(required = false) UserVo userVo) {
        JsonObject userResult = new JsonObject();
        try {
            if (Objects.isNull(userVo)) {
                throw new Exception("파라미터 null");
            }

            UserVo userCheck = new UserVo();
            userCheck.setUserId(userVo.getUserId());
            userCheck.setUserPassword(userVo.getUserPassword());

            userCheck = userService.userInfo(userCheck);
            if (Objects.isNull(userCheck)) {
                throw new Exception("삭제할 계정이 존재하지 않습니다.");
            } else {
                userService.userDelete(userVo);
                userResult.addProperty("result", true);
            }

        } catch (Exception e) {
            // TODO: handle exception
            userResult.addProperty("result", false);
            userResult.addProperty("error", e.getMessage());
            return new ResponseEntity<>(userResult.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userResult.toString(), HttpStatus.OK);
    }

    public String errorMethod(String errorType, Exception e) {
        JsonObject errorResult = new JsonObject();

        if (e != null) {
            errorResult.addProperty("result", false);
            errorResult.addProperty("error", e.getMessage());
        } else if ("login".equals(errorType)) {
            errorResult.addProperty("result", false);
            errorResult.addProperty("error", "계정이 존재하지 않습니다.");
        }

        return errorResult.toString();
    }
}
