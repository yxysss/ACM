class Test extends TestFather{

    // 静态方法的调用在编译器确定，无法实现多态

    // private int i;

    static {
        System.out.println("Son static code block");
    }

    {
        // i = 1;
        System.out.println("Son non-static code block");
    }


    public Test() {
        System.out.println("Son no-parameter init function");
    }


    public Test(String test) {
        System.out.println("Son parameter init function");
    }

    private class innerClass1 {
        // 成员内部类的属性和方法的修饰符 并无什么作用
        private int x;

        // private static int y;
        // Compilation Error: prooperty of non-static inner class cannot be modified by static

        innerClass1() {
            // Test.innerClass2.innerClassOfinnerClass2 innerClassOfinnerClass2 = new Test.innerClass2.innerClassOfinnerClass2();
            // Compilation Error: not an enclosing class
        }

        void method1() {

        }
    }

    class innerClass2 {
        innerClass2() {
            Test.innerClass1 innerClass1 = new innerClass1();
        }

        private class innerClassOfinnerClass2 {
            innerClassOfinnerClass2() {
                Test.innerClass1 innerClass1 = new innerClass1();
            }
        }
    }

    // 1. 父类的静态代码块，子类的静态代码块。。。
    // 2. 父类的非静态代码块，父类的无参构造函数。。。
    // 3. 子类的非静态代码块，子类的构造函数

    public static void main(String args[]) {
        int a = 1;
        boolean b = false; // JAVA中boolean无法转换为int型
        char c = 2;

        int d = a + c;
        // int e = a + b;
        // int f = b + c;

        System.out.println("Son main code block");

        new Test("test");
    }

}

// 声明public的类的名称必须与文件名相同
class subTest {

    private void method() {
        // Test.innerClass1 innerClass1 = new Test.innerClass1();
        // Compilation Error: Test.innerClass1 is not an enclosing class.
        // 要么定义innerClass1为静态内部类，要么定义外部类
        // 成员内部类无法在外部类之外使用
    }

    public static void main(String args[]) {
        System.out.println("subTest");
    }
}
