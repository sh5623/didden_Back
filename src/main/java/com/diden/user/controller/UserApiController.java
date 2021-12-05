package com.diden.user.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.diden.config.vo.TokenVo;
import com.diden.user.service.UserService;
import com.diden.user.vo.UserVo;
import com.diden.utils.JwtTokenUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping(value = "/user/list", produces = "application/json; charset=UTF-8")
    public String userList() {
        List<UserVo> userVoList = userService.userList();
        JsonObject userJsonList = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        userVoList.stream().forEach(userVoData -> {
            Gson gson = new Gson();
            JsonElement userJsonData = new JsonParser().parse(gson.toJson(userVoData));
            jsonArray.add(userJsonData);
        });

        userJsonList.add("data", jsonArray);
        return userJsonList.toString();
    }

    // Exception 어노테이션.
    // Json Exception 공통.
    // View Exception 공통.
    @PostMapping(value = "/user/login")
    public ResponseEntity<String> userLogin(@RequestBody(required = false) UserVo userVo, HttpServletRequest request)
            throws Exception {
        try {
            JsonObject userResult = new JsonObject();

            if (Objects.isNull(userVo)) {
                throw new Exception("파라미터 null");
            }

            UserVo userVoData = userService.userInfo(userVo);
            if (Objects.isNull(userVoData)) {
                return new ResponseEntity<>(errorMethod("login", null), HttpStatus.OK);
            }

            if (Objects.toString(userVo.getUserId(), "").equals(userVoData.getUserId())) {
                if (Objects.toString(userVo.getUserPassword(), "").equals(userVoData.getUserPassword())) {
                    TokenVo token = new TokenVo();
                    token.setAccessJwsToken(jwtTokenUtil.makeJwtAccToken(userVo).getAccessJwsToken());
                    token.setRefreshJwsToken(jwtTokenUtil.makeJwtRefToken(userVo).getRefreshJwsToken());

                    userResult.addProperty("result", true);
                    userResult.addProperty("token_acc", token.getAccessJwsToken());
                    userResult.addProperty("token_ref", token.getRefreshJwsToken());

                    userVo.setUserRefreshToken(token.getRefreshJwsToken());
                    userService.userRefTokenUpdate(userVo);

                    return new ResponseEntity<>(userResult.toString(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(errorMethod("login", null), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(errorMethod("login", null), HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(errorMethod("", e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/user/logout")
    public ResponseEntity<String> userLogout(@RequestBody(required = false) UserVo userVo, HttpServletRequest request)
            throws Exception {
        try {
            if (!Objects.toString(userVo.getUserId(), "").equals("")
                    && !Objects.toString(userVo.getUserPassword(), "").equals("")) {
                UserVo getUserVo = userService.userInfo(userVo);
                getUserVo.setUserRefreshToken("");
                userService.userUpdate(getUserVo);

                return new ResponseEntity<>("", HttpStatus.OK);
            } else {
                throw new Exception("잘못된 접근.");
            }

        } catch (Exception e) {
            return new ResponseEntity<>(errorMethod("", e), HttpStatus.OK);
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
