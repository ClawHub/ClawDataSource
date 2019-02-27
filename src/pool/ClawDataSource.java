package pool;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * <Description>自定义数据库连接池<br>
 *
 * @author clawhub<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019-02-25 10:33 <br>
 */
public class ClawDataSource implements DataSource {
    /**
     * The PrintWriter to which log messages should be directed.
     */
    private PrintWriter logWriter = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
    /**
     * 队列，用于存储连接
     */
    private LinkedBlockingQueue<Connection> holder;
    /**
     * 参数
     */
    private PoolConfig poolConfig;
    /**
     * The Jdbc url.
     */
    private String jdbcUrl;
    /**
     * The Connect properties.
     */
    private Properties connectProperties;
    /**
     * The Username.
     */
    private String username;
    /**
     * The Password.
     */
    private String password;
    /**
     * The Driver.
     */
    private Driver driver;

    /**
     * Pool config my data source.
     *
     * @param poolConfig the pool config
     * @return the my data source
     */
    public ClawDataSource poolConfig(PoolConfig poolConfig) {
        this.poolConfig = poolConfig;
        //初始化连接数小于最大连接数
        if (poolConfig.getInitialSize() > poolConfig.getMaxActive()) {
            poolConfig.initialSize(poolConfig.getMaxActive());
        }
        return this;
    }

    /**
     * Username my data source.
     *
     * @param username the username
     * @return the my data source
     */
    public ClawDataSource username(String username) {
        this.username = username;
        return this;
    }

    /**
     * Password my data source.
     *
     * @param password the password
     * @return the my data source
     */
    public ClawDataSource password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Jdbc url my data source.
     *
     * @param jdbcUrl the jdbc url
     * @return the my data source
     */
    public ClawDataSource jdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    /**
     * Build my data source.
     *
     * @return the my data source
     */
    public ClawDataSource build() {
        //组装参数
        connectProperties = new Properties();
        connectProperties.put("user", username);
        connectProperties.put("password", password);

        //如果最大连接数是负数，则不限制线程池大小
        if (poolConfig.getMaxActive() < 0) {
            holder = new LinkedBlockingQueue<>();
        } else {
            //设置连接池最大值=最大活跃连接数
            holder = new LinkedBlockingQueue<>(poolConfig.getMaxActive());
        }

        //初始化连接池
        try {
            init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 初始化连接池
     *
     * @throws SQLException the sql exception
     */
    private void init() throws SQLException {
        //获取驱动名称
        String driverClass = DriverUtil.getDriverClassName(jdbcUrl);
        //获取驱动
        driver = DriverUtil.createDriver(null, driverClass);
        //初始化连接
        for (int i = 0; i < poolConfig.getInitialSize(); ++i) {
            //创建连接
            Connection connection = createConnection();
            //加到连接池
            holder.offer(connection);
        }
    }


    /**
     * 创建连接
     *
     * @return the connection
     * @throws SQLException the sql exception
     */
    private Connection createConnection() throws SQLException {
        return driver.connect(jdbcUrl, connectProperties);
    }


    @Override
    public Connection getConnection() throws SQLException {
        //连接
        Connection connection = holder.poll();
        System.out.println("getConnection:" + connection);
        //如果获取的连接为空，则证明当前连接数量达到最大值
        if (connection == null) {
            // 当池消耗殆尽且消耗的策略，默认策略1
            //     * 0：WHEN_EXHAUSTED_FAIL
            //     * 1：WHEN_EXHAUSTED_BLOCK
            //     * 2：WHEN_EXHAUSTED_GROW
            int strategy = poolConfig.getWhenExhaustedStrategy();
            System.out.println("strategy:" + strategy);
            //根据策略处理连接
            if (strategy == PoolConstant.WHEN_EXHAUSTED_FAIL) {
                //返回异常
                throw new SQLException("WHEN_EXHAUSTED_FAIL");
            } else if (strategy == PoolConstant.WHEN_EXHAUSTED_BLOCK) {
                //阻塞到最长等待时间
                long expectGetTime = System.nanoTime() + poolConfig.getMaxWaitMillis();
                while (expectGetTime < System.nanoTime()) {
                    //从连接池中获取连接
                    connection = holder.poll();
                    //如果获取到连接
                    if (connection != null) {
                        //返回此连接
                        return connection;
                    }
                }
            } else if (strategy == PoolConstant.WHEN_EXHAUSTED_GROW) {
                //新创建一个连接
                return createConnection();
            } else {
                throw new SQLException("不支持的策略");
            }
        }
        //返回连接对象
        return connection;
    }


    /**
     * Free connection.
     *
     * @param connection the connection
     */
    public void freeConnection(Connection connection) {
        //加入连接池，如果超过最大连接数，直接扔掉
        holder.offer(connection);
    }


    @Override
    public Connection getConnection(String username, String password) {
        throw new UnsupportedOperationException("Not supported by DataSource");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("BasicDataSource is not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() {
        return logWriter;
    }

    @Override
    public void setLogWriter(PrintWriter out) {
        logWriter = out;
    }

    @Override
    public void setLoginTimeout(int seconds) {
        throw new UnsupportedOperationException("Not supported by BasicDataSource");
    }

    @Override
    public int getLoginTimeout() {
        throw new UnsupportedOperationException("Not supported by BasicDataSource");
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
