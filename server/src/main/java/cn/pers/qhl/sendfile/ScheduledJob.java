package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;

@Component
@EnableScheduling
public class ScheduledJob implements SchedulingConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

//    private long ttl = 1000 * 60 * 60 * 24;  // 24小时过期
    @Autowired
    private SendFileConfig config;

//    @Scheduled(fixedRate = 1000 * 60 * 60)   // 每1小时检查一次
//    void cleanShares() {
//        logger.debug("检测过期的共享");
//        long ttl = config.getShare().getTtl() * 24 * 60 * 60 * 1000;
//        File repo = new File(Util.REPO_ROOT);
//        File[] dirs = repo.listFiles();
//        if (dirs != null) {
//            for (File shareDir : repo.listFiles()) {
//                long duration = System.currentTimeMillis() - shareDir.lastModified();
//                if (duration > ttl) {
//                    logger.debug("删除" + shareDir);
//                    FileSystemUtils.deleteRecursively(shareDir);
//                }
//            }
//        }
//    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        if (config.getShare().getTtl() > 0) {
            logger.debug("注册扫描定时任务");
            scheduledTaskRegistrar.addFixedRateTask(() -> {
                logger.debug("检测过期的共享");
                long ttl = new Double(config.getShare().getTtl() * 24 * 60 * 60 * 1000).longValue();
                File repo = new File(Util.REPO_ROOT);
                File[] dirs = repo.listFiles();
                if (dirs != null) {
                    for (File shareDir : repo.listFiles()) {
                        long duration = System.currentTimeMillis() - shareDir.lastModified();
                        if (duration > ttl) {
                            logger.debug("删除" + shareDir);
                            FileSystemUtils.deleteRecursively(shareDir);
                        }
                    }
                }
            }, new Double(config.getShare().getScanInterval() * 60 * 60 * 1000).longValue());
        }
    }
}
