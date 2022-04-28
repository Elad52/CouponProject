package db;

/*
Created by Elad on 27/03/2022

*/

import exceptions.Errors;
import exceptions.CouponsException;
import tasks.CouponExpirationDailyJob;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtils {
    private static final Thread t1 = new Thread(new CouponExpirationDailyJob());

    public static final String URL = "jdbc:mysql://localhost:3306?createDatabaseIfNotExist=TRUE&useTimezone=TRUE&serverTimezone=UTC";
    public static final String USER = "root";
    public static final String PASSWORD = "1234";
    private static final String QUERY_CREATE_SCHEMA = "CREATE SCHEMA `coupondb`;";
    private static final String QUERY_DROP_SCHEMA = "DROP DATABASE `coupondb`;";
    private static final String QUERY_CHECK_TABLE_EXISTS = "SHOW TABLES FROM coupondb LIKE ?;";
    private static final String QUERY_CHECK_SCHEMA_EXISTS = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'coupondb';";
    private static final String QUERY_CREATE_TABLE_COMPANIES = "" +
            "CREATE TABLE `coupondb`.`companies` (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `name` VARCHAR(30) NOT NULL,\n" +
            "  `email` VARCHAR(30) NOT NULL,\n" +
            "  `password` VARCHAR(30) NOT NULL,\n" +
            "  PRIMARY KEY (`id`));";
    private static final String QUERY_CREATE_TABLE_CUSTOMERS = "" +
            "CREATE TABLE `coupondb`.`customers` (\n" +
            "        `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "        `first_name` VARCHAR(30) NOT NULL,\n" +
            "        `last_name` VARCHAR(30) NOT NULL,\n" +
            "        `email` VARCHAR(30) NOT NULL,\n" +
            "        `password` VARCHAR(30) NOT NULL,\n" +
            "        PRIMARY KEY (`id`));";
    private static final String QUERY_CREATE_TABLE_CATEGORIES = "" +
            "CREATE TABLE `coupondb`.`categories` (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `name` VARCHAR(30) NOT NULL,\n" +
            "  PRIMARY KEY (`id`));";
    private static final String QUERY_CREATE_TABLE_COUPONS = "" +
            "CREATE TABLE `coupondb`.`coupons` (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `company_id` INT NOT NULL,\n" +
            "  `category_id` INT NOT NULL,\n" +
            "  `title` VARCHAR(30) NOT NULL,\n" +
            "  `description` VARCHAR(30) NOT NULL,\n" +
            "  `start_date` DATE NOT NULL,\n" +
            "  `end_date` DATE NOT NULL,\n" +
            "  `amount` INT NOT NULL,\n" +
            "  `price` DOUBLE NOT NULL,\n" +
            "  `image` VARCHAR(30) NOT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  INDEX `company_id_idx` (`company_id` ASC) VISIBLE,\n" +
            "  INDEX `category_id_idx` (`category_id` ASC) VISIBLE,\n" +
            "  CONSTRAINT `company_id`\n" +
            "    FOREIGN KEY (`company_id`)\n" +
            "    REFERENCES `coupondb`.`companies` (`id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `category_id`\n" +
            "    FOREIGN KEY (`category_id`)\n" +
            "    REFERENCES `coupondb`.`categories` (`id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);\n";
    private static final String QUERY_CREATE_TABLE_CUSTOMERS_VS_COUPONS = "" +
            "CREATE TABLE `coupondb`.`customers_vs_coupons` (\n" +
            "  `customer_id` INT NOT NULL,\n" +
            "  `coupon_id` INT NOT NULL,\n" +
            "  PRIMARY KEY (`customer_id`, `coupon_id`),\n" +
            "  INDEX `coupon_id_idx` (`coupon_id` ASC) VISIBLE,\n" +
            "  CONSTRAINT `customer_id`\n" +
            "    FOREIGN KEY (`customer_id`)\n" +
            "    REFERENCES `coupondb`.`customers` (`id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `coupon_id`\n" +
            "    FOREIGN KEY (`coupon_id`)\n" +
            "    REFERENCES `coupondb`.`coupons` (`id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);";

    public static void databaseStrategy() throws SQLException, InterruptedException, CouponsException {
        t1.start();
        if(checkForSchema()) {
            execute(QUERY_DROP_SCHEMA);
        }
        if(!checkForSchema()) {
            execute(QUERY_CREATE_SCHEMA);
        }else {
            throw new CouponsException(Errors.SCHEMA_ALREADY_EXIST.getErrorMessage());
        }
        if(checkForTables("companies") || checkForTables("customers") ||
                checkForTables("categories") || checkForTables("coupons") ||
                checkForTables("customers_vs_coupons")) {
            throw new CouponsException(Errors.TABLE_ALREADY_EXIST.getErrorMessage());
        }else {
            execute(QUERY_CREATE_TABLE_COMPANIES);
            execute(QUERY_CREATE_TABLE_CUSTOMERS);
            execute(QUERY_CREATE_TABLE_CATEGORIES);
            execute(QUERY_CREATE_TABLE_COUPONS);
            execute(QUERY_CREATE_TABLE_CUSTOMERS_VS_COUPONS);
        }
    }

    public static Connection getConnection() throws SQLException, InterruptedException {
        return ConnectionPool.getInstance().getConnection();
    }

    public static void closeConnection(Connection conn) throws SQLException {
        ConnectionPool.getInstance().restoreConnection(conn);
    }

    public static void closeResultSet(ResultSet resultSet) throws SQLException {
        resultSet.close();
    }

    public static void closePreparedStatement(PreparedStatement statement) throws SQLException {
        statement.close();
    }

    public static void closeResources(Connection conn, PreparedStatement statement) throws SQLException {
        closePreparedStatement(statement);
        closeConnection(conn);
    }

    public static void closeResources(Connection conn, PreparedStatement statement,ResultSet resultSet) throws SQLException {
        closeResultSet(resultSet);
        closeResources(conn,statement);
    }

    public static void execute(String sql) throws SQLException, InterruptedException {
        // Step 2 - Open connection
        Connection conn = getConnection();

        // Step 3 - Prepare statement
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.execute();
        // Step 4 - Only for select - resource getting

        // Step 5 - close resources
        closeResources(conn, statement);
    }

    public static List<?> executeResults(String sql) throws SQLException, InterruptedException {
        // Step 2 - Open connection
        Connection conn = getConnection();

        // Step 3 - Prepare statement
        PreparedStatement statement = conn.prepareStatement(sql);

        // Step 4 - Only for select - resource getting
        ResultSet resultSet = statement.executeQuery();
        List<?> res = resultSetToArrayList(resultSet);

        // Step 5 - close resources
        closeResources(conn, statement,resultSet);
        return res;
    }

    public static List<?> executeResults(String sql, Map<Integer,Object> map) throws SQLException, InterruptedException {
        // Step 2 - Open connection
        Connection conn = getConnection();

        // Step 3 - Prepare statement
        PreparedStatement statement = conn.prepareStatement(sql);
        addParams(statement,map);

        // Step 4 - Only for select - resource getting
        ResultSet resultSet = statement.executeQuery();
        List<?> res = resultSetToArrayList(resultSet);

        // Step 5 - close resources
        closeResources(conn, statement,resultSet);
        return res;
    }

    public static void execute(String sql, Map<Integer, Object> map) throws SQLException, InterruptedException {
        // Step 2 - Open connection
        Connection conn = getConnection();

        // Step 3 - Prepare statement
        PreparedStatement statement = conn.prepareStatement(sql);
        addParams(statement, map);
        statement.execute();
        // Step 4 - Only for select - resource getting

        // Step 5 - close resources
        closeResources(conn, statement);
    }

    public static void addParams(PreparedStatement statement, Map<Integer, Object> map) throws SQLException {
        for (Map.Entry<Integer, Object> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Object obj = entry.getValue();
            if (obj instanceof Integer) {
                statement.setInt(key, (int) obj);
            } else if (obj instanceof String) {
                statement.setString(key, (String) obj);
            } else if (obj instanceof Double) {
                statement.setDouble(key, (double) obj);
            } else if (obj instanceof Float) {
                statement.setDouble(key, (Float) obj);
            }
        }
    }

    public static List<?> resultSetToArrayList(ResultSet rs) throws SQLException {

        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();

        List<HashMap<String, Object>> list = new ArrayList<>();

        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }

    public static boolean checkForTables(String tableName) throws SQLException, InterruptedException {
        Map<Integer,Object> params = new HashMap<>();

        params.put(1,tableName);
        List<?> rows = JDBCUtils.executeResults(QUERY_CHECK_TABLE_EXISTS,params);

        return rows.size() > 0;
    }

    public static boolean checkForSchema() throws SQLException, InterruptedException {
        List<?> rows = JDBCUtils.executeResults(QUERY_CHECK_SCHEMA_EXISTS);

        return rows.size() > 0;
    }
}

