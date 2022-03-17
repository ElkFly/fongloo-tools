package com.fongloo.log.event;

import com.fongloo.log.entity.OptLogDTO;
import org.springframework.context.ApplicationEvent;

/**
 * 日志事件
 */
public class SysLogEvent extends ApplicationEvent {
    public SysLogEvent(OptLogDTO optLogDTO) {
        super(optLogDTO);
    };
}
