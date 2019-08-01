package cn.pers.qhl.sendfile;

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

//    public static String parseDuration(String duration) {
//        PeriodFormatter formatter = new PeriodFormatterBuilder()
//                .appendMonths().appendSuffix("M").appendSeparator(" ")
//                .appendDays().appendSuffix("D").appendSeparator(" ")
//                .appendHours().appendSuffix("H").appendSeparator(" ")
//                .appendMinutes().appendSuffix("M").appendSeparator(" ")
//                .appendSeconds().appendSuffix("S").toFormatter();
//        Period period = formatter.parsePeriod(duration);
//
//        String humanReadable = "";
//        humanReadable += period.getMonths() == 0 ? "" : (period.getMonths() + "月");
//        humanReadable += period.getDays() == 0 ? "" : (period.getDays() + "天");
//        humanReadable += period.getHours() == 0 ? "" : (period.getHours() + "小时");
//        humanReadable += period.getMinutes() == 0 ? "" : (period.getMinutes() + "分钟");
//        humanReadable += period.getSeconds() == 0 ? "" : (period.getSeconds() + "秒");
//
//        return humanReadable;
//    }
}
