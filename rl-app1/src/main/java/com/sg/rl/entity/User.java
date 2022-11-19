package com.sg.rl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sg.rl.enums.SexEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //lombok注解
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@TableName("t_user")
public class User {
    @TableId(value ="id", type = IdType.AUTO)
    private Long id;

    @TableField("user_name")
    private String name;
    private Integer age;
    private String email;

    @TableLogic
    private Integer isDeleted;

    @EnumValue
    private SexEnums sex;
}