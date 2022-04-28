package db;

/*
Created by Elad on 27/03/2022

*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConnectionPool {
    private static ConnectionPool instance = null;
    private static final int SIZE = 10;
    private Stack<Connection> connections = new Stack<>();

    private ConnectionPool() throws SQLException {
        initAllConnections();
    }

    public static ConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws InterruptedException {
        synchronized (connections) {
            if (connections.empty()) {
                connections.wait();
            }
            return connections.pop();
        }
    }

    public void restoreConnection(Connection connection) {
        synchronized (connections) {
            connections.push(connection);
            connections.notify();
        }
    }

    public void initAllConnections() throws SQLException {
        for (int i = 0; i < SIZE; i++) {
            this.connections.push(DriverManager.getConnection(JDBCUtils.URL,JDBCUtils.USER,JDBCUtils.PASSWORD));
        }
    }

    public void closeAllConnections() {
        if (this.connections.size() == SIZE) {
            for (int i = 0; i < SIZE; i++) {
                this.connections.pop();
            }
        }
    }

}
