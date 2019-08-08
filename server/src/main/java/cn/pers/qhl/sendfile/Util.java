package cn.pers.qhl.sendfile;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;

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

    public static String genToken() {
//        return RandomStringUtils.random(32, "abcdefghijkmnpqrstuvwxyz23456789");
        return RandomStringUtils.randomAlphanumeric(32);
    }
}
