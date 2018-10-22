package cn.tanlw.db.lastInsertId.target;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date 2018/9/3 16:46
 */
public class LastInsertIdUtil {
    private static volatile Map<String, String> concurrentHashMap = new ConcurrentHashMap();
    private static volatile Object lock = new Object();

    public LastInsertIdUtil() {
    }

    public static Integer getPrimaryId(DataSource dataSource, String tableName) {
        Integer id = null;
        Connection connection;
        PreparedStatement preparedStatement;

        try {
            connection = dataSource.getConnection();
            if (!concurrentHashMap.containsKey(tableName)) {
                Object var5 = lock;
                synchronized(lock) {
                    if (!concurrentHashMap.containsKey(tableName)) {
                        preparedStatement = connection.prepareStatement("select count(1) from t_sequence where table_name = ? ");
                        preparedStatement.setString(1, tableName);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet != null) {
                            while(resultSet.next()) {
                                int record = resultSet.getInt(1);
                                if (1 != record) {
                                    preparedStatement = connection.prepareStatement("insert t_sequence(table_name, counter) values (?,?)");
                                    preparedStatement.setString(1, tableName);
                                    preparedStatement.setInt(2, 0);
                                    preparedStatement.executeUpdate();
                                    concurrentHashMap.put(tableName, "exist");
                                }
                            }
                        }
                    }
                }
            }

            preparedStatement = connection.prepareStatement("update t_sequence set counter=last_insert_id(counter + ?) where table_name= ?");
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, tableName);
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement("select last_insert_id()");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                while(resultSet.next()) {
                    id = resultSet.getInt(1);
                }
            }
        } catch (SQLException var19) {
            var19.printStackTrace();
        }
        return id;
    }
}
