package cn.pers.qhl.sendfile;

import org.apache.commons.lang3.RandomStringUtils;

public class Util {

    public static String REPO_ROOT = "./repo";
    public static String SHARE_INFO_FILE = "share_info.json";
    public static String FILES_DIR = "files";

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
