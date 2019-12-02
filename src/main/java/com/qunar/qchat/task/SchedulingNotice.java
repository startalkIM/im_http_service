package com.qunar.qchat.task;

import com.qunar.qchat.service.SchedulingNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SchedulingNotice {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingNotice.class);

    @Autowired
    private SchedulingNoticeService schedulingNoticeService;

    public  void process() {
        LOGGER.info("task begin");
        schedulingNoticeService.processTask();
    }
}
