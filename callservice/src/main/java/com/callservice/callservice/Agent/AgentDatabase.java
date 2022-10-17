package com.callservice.callservice.Agent;
import java.sql.*;
import java.text.*;

public class AgentDatabase {

    private static String dbName = null;
    private static int maxIndex = -1;
    /*This method will connect to existing database. If one does not exist
      it will be created. It will also create a table for the database*/
    public static void AgentDatabase(String fileName) {

        //Beginning of connection.
        String url = "jdbc:sqlite:"+ fileName;
        dbName = url;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");

         //Beginning of table creation.
                Statement statement = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS AGENTS " +
                        "(ID INTEGER PRIMARY KEY NOT NULL, "+
                        "NAME TEXT NOT NULL, "+
                        "ID_NUMBER INTEGER NOT NULL, "+
                        "STATUS TEXT NOT NULL, "+
                        "DATE_TIME DATETIME NOT NULL) ";
                statement.executeUpdate(sql);
                statement.close();
                conn.close();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Table created successfully");
    }

    //This method inserts initial data into the table
        public static void insert(Agent a) {
            try {
                Connection c = null;
                Statement stmt = null;
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection(dbName);
                c.setAutoCommit(false);
                System.out.println("Opened database successfully");

                if(a.getStore() <= maxIndex) {
                    update(a);
                }
                int ref = a.getStore();
                String name = a.getName();
                long id = a.getId();
                String status = a.getStatus();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String ts = sdf.format(timestamp);
                maxIndex = ref;

                stmt = c.createStatement();
                String sql = "INSERT INTO AGENTS (ID,NAME,ID_NUMBER,STATUS,DATE_TIME) " +
                        "VALUES (" +  ref + ", " + name + ", " + id + ", " + status + ", " + ts + ");";
                stmt.executeUpdate(sql);

                stmt.close();
                c.commit();
                c.close();
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // This method will fetch and display records
    public static void select(Agent a) {

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:AgentDatabase.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            String s = "SELECT * FROM AGENTS;";
            if(a != null) {
                int ref = a.getStore();
                s = "SELECT * FROM AGENTS WHERE ID = " + ref + ";";
            }
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(s);

            while ( rs.next() ) {
                int id = rs.getInt("ID");
                String  name = rs.getString("NAME");
                int ID_Number  = rs.getInt("ID_NUMBER");
                String  status = rs.getString("STATUS");
                String date_time = rs.getString("DATE_TIME");

                System.out.println( "ID = " + id );
                System.out.println( "NAME = " + name );
                System.out.println( "ID_NUMBER = " + ID_Number );
                System.out.println( "STATUS = " + status );
                System.out.println( "DATE_TIME = " + date_time );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    //This method will update database information
    public static void update(Agent a){

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(dbName);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String ts = sdf.format(timestamp);

            stmt = c.createStatement();
            int id1 = a.getStore();
            String sql = "UPDATE AGENTS set STATUS = " + a.getStatus() + ", DATE_TIME = " + ts + " where ID = " + id1 + ";";
            stmt.executeUpdate(sql);
            c.commit();

            ResultSet rs = stmt.executeQuery( "SELECT * FROM AGENTS;" );

            while ( rs.next() ) {
                int id = rs.getInt("ID");
                String  name = rs.getString("NAME");
                int ID_Number  = rs.getInt("ID_NUMBER");
                String  status = rs.getString("STATUS");
                String date_time = rs.getString("DATE_TIME");

                System.out.println( "ID = " + id );
                System.out.println( "NAME = " + name );
                System.out.println( "ID_NUMBER = " + ID_Number );
                System.out.println( "STATUS = " + status );
                System.out.println( "DATE_TIME = " + date_time );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    //This method will delete individual records
    public static void delete(Agent a) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:AgentDatabase.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            int ref = a.getStore();
            String sql = "DELETE from AGENTS where ID = " + ref + ";";
            stmt.executeUpdate(sql);
            c.commit();

            ResultSet rs = stmt.executeQuery( "SELECT * FROM AGENTS;" );

            while ( rs.next() ) {
                int id = rs.getInt("ID");
                String  name = rs.getString("NAME");
                int ID_Number  = rs.getInt("ID_NUMBER");
                String  status = rs.getString("STATUS");
                String date_time = rs.getString("DATE_TIME");

                System.out.println( "ID = " + id );
                System.out.println( "NAME = " + name );
                System.out.println( "ID_NUMBER = " + ID_Number );
                System.out.println( "STATUS = " + status );
                System.out.println( "DATE_TIME = " + date_time );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }


    public static void main(String[] args){
        AgentDatabase("AgentDatabase.db");
    }
}
