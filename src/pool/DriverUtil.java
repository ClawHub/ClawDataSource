package pool;

import java.sql.Driver;
import java.sql.SQLException;

/**
 * <Description>DriverHandler<br>
 *
 * @author clawhub<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019-02-25 11:04 <br>
 */
public class DriverUtil {
    /**
     * The constant mysql_driver_version_6.
     */
    private static Boolean mysql_driver_version_6 = null;
    /**
     * The constant MYSQL_DRIVER.
     */
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    /**
     * The constant MYSQL_DRIVER_6.
     */
    private static final String MYSQL_DRIVER_6 = "com.mysql.cj.jdbc.Driver";

    /**
     * Gets driver class name.
     *
     * @param rawUrl the raw url
     * @return the driver class name
     * @throws SQLException the sql exception
     */
    public static String getDriverClassName(String rawUrl) throws SQLException {
        if (rawUrl == null) {
            return null;
        }

        if (rawUrl.startsWith("jdbc:mysql:")) {
            if (mysql_driver_version_6 == null) {
                mysql_driver_version_6 = loadClass("com.mysql.cj.jdbc.Driver") != null;
            }

            if (mysql_driver_version_6) {
                return MYSQL_DRIVER_6;
            } else {
                return MYSQL_DRIVER;
            }
        } else {
            throw new SQLException("DataSource Unsupported Driver.");
        }
    }

    /**
     * Create driver driver.
     *
     * @param classLoader     the class loader
     * @param driverClassName the driver class name
     * @return the driver
     * @throws SQLException the sql exception
     */
    public static Driver createDriver(ClassLoader classLoader, String driverClassName) throws SQLException {
        Class<?> clazz = null;
        if (classLoader != null) {
            try {
                clazz = classLoader.loadClass(driverClassName);
            } catch (ClassNotFoundException e) {
                // skip
            }
        }

        if (clazz == null) {
            try {
                ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
                if (contextLoader != null) {
                    clazz = contextLoader.loadClass(driverClassName);
                }
            } catch (ClassNotFoundException e) {
                // skip
            }
        }

        if (clazz == null) {
            try {
                clazz = Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
                throw new SQLException(e.getMessage(), e);
            }
        }

        try {
            return (Driver) clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    /**
     * Load class class.
     *
     * @param className the class name
     * @return the class
     */
    private static Class<?> loadClass(String className) {
        Class<?> clazz = null;

        if (className == null) {
            return null;
        }

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            // skip
        }

        ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
        if (ctxClassLoader != null) {
            try {
                clazz = ctxClassLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                // skip
            }
        }

        return clazz;
    }
}
