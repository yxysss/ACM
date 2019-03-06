public class VolatileTest {

    /*
    public volatile VolatileTest volatileTest;
    public int a;

    public void method() {
        a = 1;
        int b = a;
        volatileTest = new VolatileTest();
        // volatileTest.a = 2;
        // volatileTest.volatileTest = new VolatileTest();
    }
    */

    public static volatile int i = 0;

    public static int j = 0, k = 0;

    int a, b;
    volatile int v, u;
    void f() {
        int i, j;

        i = a;
        j = b;
        i = v;

        j = u;

        a = i;
        b = j;

        v = i;

        u = j;

        i = u;


        j = b;
        a = i;
    }

    public static void main(String[] args) {
        // new VolatileTest().method();
        new Thread() {
            @Override
            public void run() {
                j = 1;
                i++;
                k = 2;
            }
        }.start();
        j = 1;
        i ++;
        k = 2;
    }

}
