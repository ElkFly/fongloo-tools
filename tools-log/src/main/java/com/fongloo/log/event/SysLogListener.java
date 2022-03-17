package com.fongloo.log.event;

import com.fongloo.log.entity.OptLogDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;


/**
 * 日志监听器
 * 可以实现此方法获取到日志实体对象
 * OptLogDTO logDTO = (OptLogDTO)sysLogEvent.getSource();
 * 然后您可以处理日志保存到数据库 或者对日志进行其他处理
 */
@AllArgsConstructor
@Getter
public class SysLogListener {

    private Consumer<OptLogDTO> consumer;

    @Async
    @Order
    @EventListener(SysLogEvent.class)
    void sysLogHandel(SysLogEvent sysLogEvent) {
        OptLogDTO optLogDTO = (OptLogDTO)sysLogEvent.getSource();
        consumer.accept(optLogDTO);
    }
}
