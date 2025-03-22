package com.rose.model.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    /**
     * 用户名
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     *  确认密码
     */
    private String checkPassword;

    private static final long serialVersionUID = 1L;

}
