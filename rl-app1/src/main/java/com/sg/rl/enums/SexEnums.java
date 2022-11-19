package com.sg.rl.enums;

import lombok.Getter;

@Getter
public enum SexEnums {

    SEX_MALE("male",1),
    SEX_FEMALE("female",2);

    private String sexName;
    private Integer sexid;

    SexEnums(String sexName, Integer sexid) {
        this.sexName = sexName;
        this.sexid = sexid;
    }
}
