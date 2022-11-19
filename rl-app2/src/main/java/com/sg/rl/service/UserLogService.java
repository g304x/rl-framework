package com.sg.rl.service;

import com.sg.rl.framework.components.userLog.entity.OptLogDTO;
import com.sg.rl.framework.components.userLog.event.SysLogEvent;

public interface UserLogService {
    public void saveLog(OptLogDTO logDTO);
}
