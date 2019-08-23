package cn.pers.qhl.sendfile;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

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

    public static Long parseTtlToMillis(String ttl) {
        ttl = ttl.trim();
        char unitLetter = ttl.charAt(ttl.length() - 1);
        if (Character.isLetter(unitLetter)) {
            ChronoUnit unit;
            if (ttl.endsWith("D")) {
                unit = ChronoUnit.DAYS;
            } else if (ttl.endsWith("H")) {
                unit = ChronoUnit.HOURS;
            } else if (ttl.endsWith("M")) {
                unit = ChronoUnit.MINUTES;
            } else {
                throw new BadRequestException("无法识别单位：" + unitLetter);
            }
            Long amount = Long.parseLong(ttl.substring(0, ttl.length()-1).trim());
            return Duration.of(amount, unit).toMillis();
        } else {
            return Long.parseLong(ttl);
        }
    }
}
