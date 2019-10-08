package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@EnableScheduling
public class ScheduledJob implements SchedulingConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

    @Autowired
    private SendFileConfig config;

    @Autowired
    private ShareService shareService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        logger.debug("注册扫描定时任务");
        long interval = 0;
        try {
            interval = Util.parseTtlToMillis(config.getShare().getScanInterval());
        } catch (IllegalUnitException e) {
            throw new RuntimeException("扫描间隔配置的单位错误：" + e.getMessage());
        }
        long oneHour = 1000 * 60 * 60;
        long oneMinute = 1000 * 60;
        if (interval < oneMinute) {
            logger.warn("扫描时间设置（" + config.getShare().getScanInterval() + "）过短。已使用默认值：1小时");
            interval = oneHour;
        }
        scheduledTaskRegistrar.addFixedRateTask(() -> {
            logger.debug("检测过期的共享");
            shareService.getAllShares();
//        }, new Double(config.getShare().getScanInterval() * 60 * 60 * 1000).longValue());
        }, interval);
    }
}
