package com.sg.rl.mapper;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sg.rl.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {


    Page<User> selectPageVo(@Param("page") Page<User> page, @Param("age") Integer age);


    int insertSelective(User user);


    int deleteById(@Param("id") Long id);

}