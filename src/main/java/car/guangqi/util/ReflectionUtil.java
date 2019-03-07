package car.guangqi.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by huoshu_001 on 17/7/12.
 */
public class ReflectionUtil {
    private static Logger logger = LogManager.getLogger();

    ReflectionUtil() {

    }

    public static String getCallerClass(final int depth) {
        if (depth < 0) {
            throw new IndexOutOfBoundsException(Integer.toString(depth));
        }

        try {
            final StackTraceElement element = getEquivalentStackTraceElement(depth + 1);
            return element.getClassName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isValid(final StackTraceElement element) {
        // ignore native methods (oftentimes are repeated frames)
        if (element.isNativeMethod()) {
            return false;
        }
        final String cn = element.getClassName();
        // ignore OpenJDK internal classes involved with reflective invocation
        if (cn.startsWith("sun.reflect.")) {
            return false;
        }
        final String mn = element.getMethodName();
        // ignore use of reflection including:
        // Method.invoke
        // InvocationHandler.invoke
        // Constructor.newInstance
        if (cn.startsWith("java.lang.reflect.") && (mn.equals("invoke") || mn.equals("newInstance"))) {
            return false;
        }
        // ignore use of Java 1.9+ reflection classes
        if (cn.startsWith("jdk.internal.reflect.")) {
            return false;
        }
        // ignore Class.newInstance
        if (cn.equals("java.lang.Class") && mn.equals("newInstance")) {
            return false;
        }
        // ignore use of Java 1.7+ MethodHandle.invokeFoo() methods
        if (cn.equals("java.lang.invoke.MethodHandle") && mn.startsWith("invoke")) {
            return false;
        }
        // any others?
        return true;
    }

    static StackTraceElement getEquivalentStackTraceElement(final int depth) {
        // (MS) I tested the difference between using Throwable.getStackTrace() and Thread.getStackTrace(), and
        // the version using Throwable was surprisingly faster! at least on Java 1.8. See ReflectionBenchmark.
        final StackTraceElement[] elements = new Throwable().getStackTrace();
        int i = 0;
        for (final StackTraceElement element : elements) {
            if (isValid(element)) {
                if (i == depth) {
                    return element;
                }
                ++i;
            }
        }
        logger.error("Could not find an appropriate StackTraceElement at index {}", depth);
        throw new IndexOutOfBoundsException(Integer.toString(depth));
    }
}
