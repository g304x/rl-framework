package com.sg.rl.framework.components.userLog.event;


import com.sg.rl.framework.components.userLog.entity.OptLogDTO;
import org.springframework.context.ApplicationEvent;

/**
 * 系统日志事件
 *
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(OptLogDTO source) {
        super(source);
    }
}
