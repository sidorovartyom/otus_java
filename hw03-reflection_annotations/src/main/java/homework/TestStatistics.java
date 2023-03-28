package homework;

import homework.annotation.Before;
import homework.annotation.After;
import homework.annotation.Test;
import java.lang.reflect.Method;
import java.util.List;
import static homework.reflection.ReflectionHelper.callMethod;
import static homework.reflection.ReflectionHelper.getMethodsAnnotation;
import static homework.reflection.ReflectionHelper.instantiate;


public class TestStatistics {
    private TestStatistics() { }

    public static void runTest(String className) throws ClassNotFoundException {
        final Class<?> clazz = Class.forName(className);

        final var beforeMethods = getMethodsAnnotation(clazz, Before.class);
        final var afterMethods = getMethodsAnnotation(clazz, After.class);
        final var testMethods = getMethodsAnnotation(clazz, Test.class);

        int failed = 0;
        for (var method : testMethods) {
            Object testObject = instantiate(clazz);
            try {
                runBeforeMethods(testObject, beforeMethods);
                callMethod(testObject, method);
            } catch (Exception e) {
                System.out.println("\nTest failed: " + e.getMessage());
                failed++;
            } finally {
                runAfterMethods(testObject, afterMethods);
            }
        }
        System.out.println(getStatisticString(testMethods.size(), failed));
    }

    private static void runMethods(final Object classInstance, final List<Method> methods) {
        methods.forEach(method -> callMethod(classInstance, method));
    }

    private static void runBeforeMethods(final Object testObject, final List<Method> beforeMethods) {
        runMethods(testObject, beforeMethods);
    }

    private static void runAfterMethods(final Object testObject, final List<Method> afterMethods) {
        runMethods(testObject, afterMethods);
    }

    private static String getStatisticString(int total, int failed) {
        int successful = total - failed;
        return String.format("\nTest statistics\n\tTotal: " + total + "\n\tSuccessful: " + successful + "\n\tFailed: " + failed);
    }
}