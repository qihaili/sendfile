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
        if (config.getShare().getTtl() > 0) {
            logger.debug("注册扫描定时任务");
            scheduledTaskRegistrar.addFixedRateTask(() -> {
                logger.debug("检测过期的共享");
                List<Share> shares = shareService.getAllShares();
                for (Share share : shares) {
                    if (share.getTtl() < 0) {
                        logger.debug("删除share: " + share.getId());
                        shareService.deleteShareDir(share.getId());
                    }
                }
//                long ttl = new Double(config.getShare().getTtl() * 24 * 60 * 60 * 1000).longValue();
//                File repo = new File(Util.REPO_ROOT);
//                File[] dirs = repo.listFiles();
//                if (dirs != null) {
//                    for (File shareDir : repo.listFiles()) {
//                        long duration = System.currentTimeMillis() - shareDir.lastModified();
//                        if (duration > ttl) {
//                            logger.debug("删除" + shareDir);
//                            FileSystemUtils.deleteRecursively(shareDir);
//                        }
//                    }
//                }
            }, new Double(config.getShare().getScanInterval() * 60 * 60 * 1000).longValue());
        }
    }
}
