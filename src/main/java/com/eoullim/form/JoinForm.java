package com.eoullim.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter @Setter
public class JoinForm {
    private String name;
    private String email;
    private String loginId;
    private String loginPw;
    private String gender;
}
