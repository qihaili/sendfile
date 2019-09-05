import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import java.time.Period;
import java.time.temporal.ChronoUnit;

public class MyTest {

    public static void main(String[] args) {
        System.out.println(DataSize.parse("10"));
    }
}
