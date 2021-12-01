package com.yu.zehnit.tools;

import com.google.android.exoplayer2.C;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import javax.sql.PooledConnection;

public class ConnectionPool {
    private String jdbcDriver="com.mysql.jdbc.Driver";
    private String dbUrl="jdbc:mysql://47.100.27.103:3306/db_veriband";
    private String dbUsername="root";
    private String dbPassword="zehnit";
    private String testTable = "t_user"; // 测试连接是否可用的测试表名，默认没有测试表
    private int initialConnections = 10; // 连接池的初始大小
    private int incrementalConnections = 5; // 连接池自动增加的大小
    private int maxConnections = 50; // 连接池最大的大小
    private Vector<PooledConnection> connections = null; // 存放连接池中数据库连接的向量 , 初始时为 null
    
    public ConnectionPool(String jdbcDriver,String dbUrl,String dbUsername,String dbPassword){
        this.jdbcDriver=jdbcDriver;
        this.dbUrl=dbUrl;
        this.dbUsername=dbUsername;
        this.dbPassword=dbPassword;
    }
    public void setInitialConnections(int initialConnections){
        this.initialConnections=initialConnections;
    }
    public int getIncrementalConnections(){
        return this.incrementalConnections;
    }
    public void setIncrementalConnections(int incrementalConnections){
        this.incrementalConnections=incrementalConnections;
    }
    public int getMaxConnections(){
        return this.maxConnections;
    }
    public void setMaxConnections(int maxConnections){
        this.maxConnections=maxConnections;
    }
    public String getTestTable(){
        return this.testTable;
    }
    public void setTestTable(String testTable){
        this.testTable=testTable;
    }
    public synchronized void createPool() throws Exception{
        if (connections!=null){
            return;
        }
        Driver driver=(Driver) (Class.forName(this.jdbcDriver).newInstance());
        DriverManager.registerDriver(driver);
        connections=new Vector<PooledConnection>();
        createConnnections(this.initialConnections);
        System.out.println("数据库连接池创建成功！");
    }
    public void createConnnections(int numConnections)throws SQLException{
        for (int x=0;x<numConnections;x++){
            if (this.maxConnections>0&&this.connections.size()>=this.maxConnections){
                break;
            }
            try {
                connections.addElement(new PooledConnection(newConnections()));
            }catch (SQLException e){
                System.out.println("创建数据库链接失败！"+e.getMessage());
                throw new SQLException();
            }
            System.out.println("数据库链接已创建");
        }
        
    }
    public Connection newConnections()throws SQLException{
        Connection conn=DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
        if (connections.size()==0){
            DatabaseMetaData metaData=conn.getMetaData();
            int driverMaxConnections=metaData.getMaxConnections();
            if (driverMaxConnections>0&&this.maxConnections>driverMaxConnections){
                this.maxConnections=driverMaxConnections;
            }
        }
        return conn;
    }
    public synchronized Connection getConnection() throws SQLException{
        if (connections==null){
            return null;
        }
        Connection conn=getFreeConnection();
        while (conn==null){
            wait(250);
            conn=getFreeConnection();
        }
        return conn;
    }
    public Connection getFreeConnection() throws SQLException{
        Connection conn=findFreeConnection();
        if (conn==null){
            createConnnections(incrementalConnections);
            conn=findFreeConnection();
            if (conn==null){
                return null;
            }
        }
        return conn;
    }
    public Connection findFreeConnection() throws SQLException{
        Connection conn=null;
        PooledConnection pConn=null;
        Enumeration<PooledConnection>enumerate=connections.elements();
        while (enumerate.hasMoreElements()){
            pConn=(PooledConnection) enumerate.nextElement();
            if (!pConn.isBusy()){
                conn=pConn.getConnection();
                pConn.setBusy(true);
                if (!testConnection(conn)){
                    try {
                        conn=newConnections();
                    }catch (SQLException e){
                        System.out.println("failure"+e.getMessage());
                        return null;
                    }
                    pConn.setConnection(conn);
                }
                break;
            }
        }
        return conn;
    }
    private boolean testConnection(Connection conn){
        try {
            if (testTable.equals("")){
                conn.setAutoCommit(true);
            }else {
                Statement stmt=conn.createStatement();
                stmt.execute("select count(*) from "+testTable);
            }
        }catch (SQLException e){
            closeConnection(conn);
            return false;
        }
        return true;
    }
    public void returnConnection(Connection conn){
        if (connections==null){
            System.out.println("连接池不存在");
            return;
            
        }
        PooledConnection pConn=null;
        Enumeration<PooledConnection>enumerate=connections.elements();
        while (enumerate.hasMoreElements()){
            pConn=(PooledConnection) enumerate.nextElement();
            if (conn==pConn.getConnection()){
                pConn.setBusy(false);
                break;
            }
        }
    }
    public synchronized void refreshConnections() throws SQLException{
        if (connections==null){
            System.out.println("连接池不存在，无法刷新");
            return;
        }
        PooledConnection pConn=null;
        Enumeration<PooledConnection>enumerate=connections.elements();
        while (enumerate.hasMoreElements()){
            pConn=(PooledConnection) enumerate.nextElement();
            if (pConn.isBusy()){
                wait(5000);
            }
            closeConnection(pConn.getConnection());
            pConn.setConnection(newConnections());
            pConn.setBusy(false);
        }
    }
    public synchronized void closeConnectionPool()throws SQLException{
        if (connections==null){
            System.out.println("连接池不存在，无法关闭");
            return;
        }
        PooledConnection pConn=null;
        Enumeration<PooledConnection>enumerate=connections.elements();
        while (enumerate.hasMoreElements()){
            pConn=(PooledConnection) enumerate.nextElement();
            if (pConn.isBusy()){
                wait(5000);

            }
            closeConnection(pConn.getConnection());
            connections.removeElement(pConn);
        }
        connections=null;
    }
    public void closeConnection(Connection conn){
        try {
            conn.close();
        }catch (SQLException e){
            System.out.println("关闭数据库链接出错"+e.getMessage());
        }
    }
    public void wait(int mSeconds){
        try {
            Thread.sleep(mSeconds);
        }catch (InterruptedException e){
        }
    }
    class PooledConnection{
        Connection connection=null;
        boolean busy=false;
        public PooledConnection(Connection connection){
            this.connection=connection;
        }
        public Connection getConnection(){
            return connection;
        }
        public void setConnection(Connection connection){
            this.connection=connection;
        }
        public boolean isBusy() {
            return busy;
        }
        public void setBusy(boolean busy){
            this.busy=busy;
        }
    }
/*
    public static void main(String[] args) {
        String driverClass = "com.mysql.jdbc.Driver";
        String driverUrl = "jdbc:mysql://47.100.27.103:3306/db_veriband";
        String username = "root";
        String password = "zehnit";

        ConnectionPool connPool = new ConnectionPool(driverClass,driverUrl,username,password);

        try {
            connPool.createPool();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Connection conn = connPool.getConnection();
            System.out.println(conn);
        } catch (SQLException ex1) {
            ex1.printStackTrace();
        }
    }

 */

}
