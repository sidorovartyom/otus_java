package homework.proxy;

import homework.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.stream;

class Ioc {

    private Ioc() {
    }

    static SummatorInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new SummatorImpl());
        return (SummatorInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{SummatorInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final SummatorInterface myClass;
        private final List<Method> annotatedMethods;

        DemoInvocationHandler(SummatorInterface clazz) {

            this.myClass = clazz;
            this.annotatedMethods = getMethodsAnnotation(clazz);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            var listMethodParam = stream(method.getParameterTypes()).toList();
            for (var m : this.annotatedMethods) {
                var listAnnMethodParam = stream(m.getParameterTypes()).toList();
                if (m.getName().equals(method.getName()) && listAnnMethodParam.equals(listMethodParam)) {
                        System.out.println("\nexecuted method: " + method.getName() + ", param: " + Arrays.toString(args));
                }
            }
            return method.invoke(myClass, args);
        }

        private static List<Method> getMethodsAnnotation(SummatorInterface clazz) {
            List<Method> listMethod = new ArrayList<>();
            for (var method : clazz.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Log.class)) {
                    listMethod.add(method);
                }
            }
            return listMethod;
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
