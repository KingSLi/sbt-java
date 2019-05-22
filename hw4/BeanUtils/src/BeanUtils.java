import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils {
    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * <p/>
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * <p/>
     * The method takes care only about public method.
     *
     * @param to Object which properties will be set.
     * @param from Object which properties will be used to get values.
     */

    public static void assign(Object to, Object from) throws InvocationTargetException, IllegalAccessException {
        Class<?> clazzFrom = from.getClass();
        Class<?> clazzTo = to.getClass();
        for (Method setter: clazzTo.getMethods()) {
            if (!isSetter(setter)) {
                continue;
            }
            Class<?> paramType = setter.getParameterTypes()[0];
            try {
                Method getter = clazzFrom.getMethod("get" + setter.getName().substring(3));
                if (paramType.isAssignableFrom(getter.getReturnType())) {
                    setter.invoke(to, getter.invoke(from));
                }
            } catch (NoSuchMethodException ignored) {}
        }
    }

    private static boolean isSetter(Method method) {
        return method.getName().substring(0, 3).equals("set")
                && method.getParameterCount() == 1;
    }
}