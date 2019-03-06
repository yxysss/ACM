public class TestFather {

    // protected static int i;
    {
        // i = 2;
    }

    static {
        System.out.println("Father static code block");
    }

    {
        // i = 1;
        System.out.println("Father non-static code block");
    }


    // 子类在创建实例时需要调用父类的默认（无参）构造函数，所以父类必须有默认（无参）构造函数
    public TestFather() {
        System.out.println("Father no-parameter init function");
    }


    public TestFather(String testFather) {
        System.out.println("Father parameter init function");
    }

    public static void main(String args[]) {

        System.out.println("Father main code block");
        // new TestFather("testFather");
    }

}
