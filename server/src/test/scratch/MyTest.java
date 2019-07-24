import cn.pers.qhl.sendfile.Util;

public class MyTest {

    public static void main(String[] args) {
        for (int i=0; i<10000; i++) {
            System.out.println(Util.genId());
        }
    }
}
