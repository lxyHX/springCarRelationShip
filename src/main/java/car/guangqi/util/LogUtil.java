package car.guangqi.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lei on 16/5/10.
 */
public class LogUtil {
    private static ConcurrentHashMap<String, Logger> loggerMaps = new ConcurrentHashMap<>();

    private static Logger CreateOrGetLogger() {
        String caller = ReflectionUtil.getCallerClass(3);
        if (loggerMaps.containsKey(caller)) {
            return loggerMaps.get(caller);
        }else {
            Logger logger = LogManager.getLogger(caller);
            loggerMaps.put(caller, logger);
            return logger;
        }
    }

    public static void Info(String msg) {
        CreateOrGetLogger().info(msg);
    }

    public static void Warn(String msg) {
        CreateOrGetLogger().warn(msg);
    }

    public static void Error(String msg) {
        CreateOrGetLogger().error(msg);
    }

    public static void Debug(String msg) { CreateOrGetLogger().debug(msg);}
}
