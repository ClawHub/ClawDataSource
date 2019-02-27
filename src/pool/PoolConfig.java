package pool;

/**
 * <Description>PoolConfig<br>
 * <p>
 * ps：在此不处理空闲的连接
 *
 * @author clawhub<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019-02-26 14:50 <br>
 */
public class PoolConfig {

    /**
     * 初始化连接数：连接池启动时创建的初始化连接数量，默认值0
     */
    private int initialSize;

    /**
     * 最大连接数量：连接池在同一时间能够分配的最大活动连接的数量,，如果设置为非正数则表示不限制，默认值8
     * 表示同时最多有maxActive个数据库连接。
     */
    private int maxActive = PoolConstant.MAX_ACTIVE_DEFAULT;

    /**
     * 当池消耗殆尽且消耗的策略，默认策略1
     * 0：WHEN_EXHAUSTED_FAIL
     * 1：WHEN_EXHAUSTED_BLOCK
     * 2：WHEN_EXHAUSTED_GROW
     */
    private int whenExhaustedStrategy = PoolConstant.WHEN_EXHAUSTED_DEFAULT;
    /**
     * 当池消耗殆尽且消耗的策略是WHEN_EXHAUSTED_BLOCK，可以设置最大等待时间，
     * 如果小于0，可能会无限期阻止。
     */
    private long maxWaitMillis = PoolConstant.MAX_WAIT_MILLIS_DEFAULT;

    /**
     * 最大空闲连接：连接池中容许保持空闲状态的最大连接数量，超过的空闲连接将被释放，如果设置为负数表示不限制，默认值8
     * 表示即使没有数据库连接时依然可以保持maxIdle空闲的连接，而不被清除，随时处于待命状态
     */
    private int maxIdle = PoolConstant.MAX_IDLE_DEFAULT;

    /**
     * 最小空闲连接：连接池中容许保持空闲状态的最小连接数量，低于这个数量将创建新的连接，如果设置为0则不创建，默认值0
     * 当连接数少于此值时，连接池会创建连接来补充到该值的数量
     */
    private int minIdle;


    /**
     * Initial size pool config.
     *
     * @param initialSize the initial size
     * @return the pool config
     */
    public PoolConfig initialSize(int initialSize) {
        this.initialSize = initialSize;
        return this;
    }

    /**
     * Max wait millis pool config.
     *
     * @param maxWaitMillis the max wait millis
     * @return the pool config
     */
    public PoolConfig maxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
        return this;
    }

    /**
     * Max active pool config.
     *
     * @param maxActive the max active
     * @return the pool config
     */
    public PoolConfig maxActive(int maxActive) {
        this.maxActive = maxActive;
        return this;
    }

    /**
     * When exhausted strategy pool config.
     *
     * @param whenExhaustedStrategy the when exhausted strategy
     * @return the pool config
     */
    public PoolConfig whenExhaustedStrategy(int whenExhaustedStrategy) {
        this.whenExhaustedStrategy = whenExhaustedStrategy;
        return this;
    }

    /**
     * Build pool config.
     *
     * @return the pool config
     */
    public PoolConfig build() {
        return this;
    }

    /**
     * Gets initial size.
     *
     * @return the initial size
     */
    public int getInitialSize() {
        return initialSize;
    }

    /**
     * Gets max active.
     *
     * @return the max active
     */
    public int getMaxActive() {
        return maxActive;
    }

    /**
     * Gets when exhausted strategy.
     *
     * @return the when exhausted strategy
     */
    public int getWhenExhaustedStrategy() {
        return whenExhaustedStrategy;
    }

    /**
     * Gets max wait millis.
     *
     * @return the max wait millis
     */
    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

}
