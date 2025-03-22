package com.rose.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rose.model.VO.LoginUserVO;
import com.rose.model.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword);

    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    LoginUserVO getLoginUserVO(User user);

    boolean userLogout(HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

    User getLoginUserPermitNull(HttpServletRequest request);
}
