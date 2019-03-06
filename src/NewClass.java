public class NewClass {

    public NewClass newClass;

    public int a;

    public void method() {
        newClass = new NewClass();
    }

    public void runThread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                a = 1;
                newClass = new NewClass();
            }
        };
    }

    public static void main(String[] args) {


    }


}
