package homework.proxy;

public class Run {
    public static void main(String[] args) {
        SummatorInterface summator = Ioc.createMyClass();
        summator.summator(1);
        summator.summator(1, 2); //без лога
        summator.summator(1,2,3);
    }
}
