# ClawDataSource
数据库连接池的简单实现
- 采用LinkedBlockingQueue管理数据库连接
- 实现javax.sql.DataSource接口
- 采用构造者设计模式
- 通过反射技术获取驱动
- 兼容MYSQL5与6驱动
- 支持配置连接池资源消耗殆尽的处理策略
- 未实现空闲连接数控制
