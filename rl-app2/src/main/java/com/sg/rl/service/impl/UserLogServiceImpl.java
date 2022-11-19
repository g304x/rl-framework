package com.sg.rl.service.impl;

import com.sg.rl.framework.components.userLog.entity.OptLogDTO;
import com.sg.rl.service.UserLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserLogServiceImpl implements UserLogService {
    @Override
    public void saveLog(OptLogDTO logDTO) {
        log.info("saveLog logEvent {}",logDTO);
    }
}
