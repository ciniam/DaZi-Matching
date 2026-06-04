package com.xiejun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiejun.model.domain.Team;
import com.xiejun.model.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiejun
 */
public interface TeamMapper extends BaseMapper<Team> {
    List<User> SelectUsers(@Param("teamId") long teamId);
}




