import cn.pers.qhl.sendfile.Util;

import java.time.Period;
import java.time.temporal.ChronoUnit;

public class MyTest {

    public static void main(String[] args) {
//        System.out.println(Util.parseTtlToMillis("1W"));
        System.out.println(Period.ofYears(2).get(ChronoUnit.DAYS));
    }
}
