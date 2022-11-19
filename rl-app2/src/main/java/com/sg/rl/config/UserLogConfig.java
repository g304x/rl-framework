package com.sg.rl.config;

import com.sg.rl.framework.components.userLog.entity.OptLogDTO;
import com.sg.rl.framework.components.userLog.event.SysLogListener;
import com.sg.rl.service.UserLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class UserLogConfig {

    @Bean
    public SysLogListener getSysLogListener(UserLogService logService){
        Consumer<OptLogDTO> consumer = (opt) -> logService.saveLog(opt);
        log.info("SysLogListener init success!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return new SysLogListener(consumer);
    }

}
