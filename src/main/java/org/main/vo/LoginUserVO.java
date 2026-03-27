package org.main.vo;

import lombok.Data;

@Data
public class LoginUserVO {
    private Long id;
    private String userno;
    private String username;
    private String phone;
    private String email;
    private Integer sex;
    private String avatar;
    private Integer status;
    private String createTime;
    private String updateTime;
}
