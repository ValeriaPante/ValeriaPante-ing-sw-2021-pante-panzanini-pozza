package MessageTest;

import org.junit.jupiter.api.Test;

public class RandomTest {
    private class ClassA{
        public void print(){
            System.out.println("A");
        }
    }

    private class ClassB extends ClassA{
        public void print(){
            System.out.println("B");
        }
    }

    private class ClassC extends ClassA{
        public void print(){
            System.out.println("c");
        }
    }

    private void methodTest(ClassA classA){
        classA.print();
    }

    @Test
    public void test(){
        ClassA classB = new ClassB();
        ClassC classC = new ClassC();

        this.methodTest(classB);
        this.methodTest(classC);
    }
}
