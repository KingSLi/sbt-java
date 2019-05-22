import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) {
        A a =  new A();
        a.setTest("test");
        B b = new B();
        try {
            BeanUtils.assign(b, a);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assert (b.test.equals(a.test));
    }
}

class A {
    String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}

class B {
    String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}


