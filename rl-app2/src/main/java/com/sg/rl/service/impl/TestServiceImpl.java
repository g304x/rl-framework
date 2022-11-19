package com.sg.rl.service.impl;

import com.sg.rl.framework.components.userLog.annotation.SysLog;
import com.sg.rl.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    @SysLog("TestServiceImpl->execute")
    public void execute(String userId, Integer channel) {

    }
}
