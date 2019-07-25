package cn.pers.qhl.sendfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;

@Component
public class ScheduledJob {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

    private long ttl = 1000 * 60 * 60 * 24;  // 24小时过期

    @Scheduled(fixedRate = 1000 * 60 * 60)   // 每1小时检查一次
    void cleanShares() {
        logger.debug("检测过期的共享");
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
    }
}
