package car.guangqi.util;

import org.springframework.context.ApplicationContext;

/**
 * Created by huoshu_001 on 17/7/12.
 */
public class SpringUtil {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static void autowireBean(Object bean) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(bean);
    }

}
