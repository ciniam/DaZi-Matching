package com.xiejun.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiejun
 * @Date:2023/3/21 8:56
 */
@Data
public class UserSendMessage implements Serializable {

    private static final long serialVersionUID = 46412442243484364L;

    private String userEmail;
    private String code;

}