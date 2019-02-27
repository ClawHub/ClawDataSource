package pool;

/**
 * <Description>PoolConstant<br>
 *
 * @author clawhub<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019-02-26 14:49 <br>
 */
public class PoolConstant {
    /**
     * 到达pool的阀值，失败的处理
     */
    public static final int WHEN_EXHAUSTED_FAIL = 0;

    /**
     * 到达pool的阀值，阻止再次借用对象的处理
     */
    public static final int WHEN_EXHAUSTED_BLOCK = 1;

    /**
     * 到达pool的阀值，生成新的对象提供给外部调用者
     */
    public static final int WHEN_EXHAUSTED_GROW = 2;
    /**
     * 到达pool的阀值，默认策略1
     */
    public static final int WHEN_EXHAUSTED_DEFAULT = 1;

    /**
     * 设置能被池创建的对象的最大个数默认值
     */
    public static final int MAX_ACTIVE_DEFAULT = 8;
    /**
     * 到达pool的阀值，最大等待时间默认值
     */
    public static final long MAX_WAIT_MILLIS_DEFAULT = -1L;
    /**
     * 最大空闲数量默认值
     */
    public static final int MAX_IDLE_DEFAULT = 8;
}

