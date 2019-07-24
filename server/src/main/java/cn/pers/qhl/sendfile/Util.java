package cn.pers.qhl.sendfile;

import java.io.File;
import java.util.UUID;

public class Util {

    public static String REPO_ROOT = "./repo";

    public static File getShareDir(String shareId) {
        File shareDir = new File(REPO_ROOT, shareId);
        if (!shareDir.exists()) {
            throw new NotFoundException("未找到共享（" + shareId + "）");
        } else {
            return shareDir;
        }
    }

    public static File createShareDir() {
//        String shareId = UUID.randomUUID().toString();
        String shareId = genId();
        File shareDir = new File(REPO_ROOT, shareId);
        shareDir.mkdirs();
        return shareDir;
    }


    private static volatile long lastTime;

    public static String genId() {
        long time = System.currentTimeMillis();
        if (time > lastTime) {
            lastTime = time;
        } else {
            lastTime++;
        }
        return Long.toString(lastTime, 36);
    }
}
