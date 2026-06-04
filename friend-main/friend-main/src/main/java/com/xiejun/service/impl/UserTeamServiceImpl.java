package com.xiejun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiejun.model.domain.User;
import com.xiejun.model.request.TeamUpdateRequest;
import com.xiejun.service.UserTeamService;
import com.xiejun.model.domain.UserTeam;
import com.xiejun.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
        implements UserTeamService {

}




