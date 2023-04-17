package homework.proxy;

import homework.annotation.Log;

public class SummatorImpl implements SummatorInterface {

    @Log
    @Override
    public void summator(int param1) {
        System.out.println("Result:" + param1);
    }

    @Override
    public void summator(int param1, int param2) {
        System.out.println("Result:" + (param1 + param2));
    }

    @Log
    @Override
    public void summator(int param1, int param2, int param3) {
        System.out.println("Result:" + (param1 + param2 + param3));
    }
}
