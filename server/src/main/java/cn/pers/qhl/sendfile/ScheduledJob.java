package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.List;

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
        scheduledTaskRegistrar.addFixedRateTask(() -> {
            logger.debug("检测过期的共享");
            List<Share> shares = shareService.getAllShares();
            for (Share share : shares) {
                Long ttl = share.getTtl();
                if (ttl != null && ttl < 0) {
                    logger.debug("删除share: " + share.getId());
                    shareService.deleteShareDir(share.getId());
                }
            }
        }, new Double(config.getShare().getScanInterval() * 60 * 60 * 1000).longValue());
    }
}
