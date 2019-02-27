package pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <Description>Test<br>
 *
 * @author clawhub<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2019-02-25 20:23 <br>
 */
public class Test {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws SQLException the sql exception
     */
    public static void main(String[] args) throws SQLException {
        System.out.println("--------");
        String jdbcUrl = "jdbc:mysql://XXXX:XXXX/XXXX?autoReconnect=true&useSSL=false";
        String username = "XXXX";
        String password = "XXXX";
        int initialSize = 1;

        //新建数据源
        ClawDataSource dataSource = new ClawDataSource();
        //配置数据源
        dataSource.username(username).password(password).jdbcUrl(jdbcUrl).poolConfig(new PoolConfig().initialSize(initialSize).build()).build();
        //获取连接
        Connection connection = dataSource.getConnection();

        //执行sql
        String sql = "select  * from t_check_param";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println(resultSet.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //释放连接
            dataSource.freeConnection(connection);
        }
    }
}
