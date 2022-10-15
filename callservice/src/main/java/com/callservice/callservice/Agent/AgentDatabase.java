package com.callservice.callservice.Agent;
import java.sql.*;

public class AgentDatabase {

    /*This method will connect to existing database. If one does not exist
      it will be created. It will also create a table for the database*/
    public static void AgentDatabase(String fileName) {

        //Beginning of connection.
        String url = "jdbc:sqlite:"+ fileName;

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
        public static void insert() {
            try {
                Connection c = null;
                Statement stmt = null;
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:AgentDatabase.db");
                c.setAutoCommit(false);
                System.out.println("Opened database successfully");

                stmt = c.createStatement();
                String sql = "INSERT INTO AGENTS (ID,NAME,ID_NUMBER,STATUS,DATE_TIME) " +
                        "VALUES (1, 'Paul', 321, 'Available', '1998-02-07 12:23:00' );";
                stmt.executeUpdate(sql);

                sql = "INSERT INTO AGENTS (ID,NAME,ID_NUMBER,STATUS,DATE_TIME) " +
                        "VALUES (2, 'Allen', 125, 'Available', '1999-12-23 16:23:00' );";
                stmt.executeUpdate(sql);

                sql = "INSERT INTO AGENTS (ID,NAME,ID_NUMBER,STATUS,DATE_TIME) " +
                        "VALUES (3, 'Teddy', 123, 'Available', '1988-06-27 09:23:00' );";
                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
                c.close();
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // This method will fetch and display records
    public static void select() {

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:AgentDatabase.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
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

    //This method will update database information
    public static void update(){

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:AgentDatabase.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "UPDATE AGENTS set ID_NUMBER = 12345  where ID=1;";
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
    public static void delete() {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:AgentDatabase.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "DELETE from AGENTS where ID=2;";
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
        insert();
        select();
        update();
        delete();
    }
}
