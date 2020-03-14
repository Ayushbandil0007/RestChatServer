package dbhandler;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Ayush Bandil on 12/2/2020.
 */
public class ConnectionUtils {
    private static String url = "jdbc:sqlserver://localhost:1434;databaseName=ChatServer;";
    private static String username = "mapsuser";
    private static String password = "mapsuser";

    public static void main(String[] args) {
        ArrayList<Message> messages = getAllMessages("Not important");
        Message msg = new Message("Sports", "Yellahhhh", 1579818912, "Shali");
//        putMessage(msg);

        String user = "Ayush";
        boolean active = true;
        updateUserStatus(user, active);
        boolean isActive = isUserActive(user);
        boolean validUser = isValidUser(user, password);

    }

    public static boolean isValidUser(String user, String password) {
        String query = "select Password as value from USER_TABLE where username = '<USERNAME>'";
        query = query.replace("<USERNAME>", user);

        Connection con = ConnectionUtils.getConnection();
        String passwordDB = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                passwordDB = rs.getString("VALUE").replaceAll(" ", "");
            }
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwordDB != null && password.equals(passwordDB);
    }

    private static boolean isUserActive(String user) {
        String query = "select active as value from USER_TABLE where username = '<USERNAME>'";
        query = query.replace("<USERNAME>", user);

        double active = executeJdbcSingleOutputQuery(query);
        return active == 1;
    }

    private static void updateUserStatus(String user, boolean activeBol) {
        int active = activeBol ? 1 : 0;
        String query = "Update USER_TABLE set ACTIVE = " + active + " where USERNAME = '" + user + "'";
        executeJdbcQuery(query);
    }


    public static void executeJdbcQuery(String query) {
        Connection con = ConnectionUtils.getConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.execute();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                return connection;
            } else {
                throw new SQLException("Could not establish connection");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Message> getAllMessages(String topic) {
        String query = "Select * from MESSAGE_TABLE where TOPIC = '" + topic + "' order by TIME";
        Connection con = ConnectionUtils.getConnection();
        ArrayList<Message> messageArray = new ArrayList();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message newMessage = new Message(topic, rs.getString("MESSAGES"), rs.getInt("TIME"), rs.getString("USERNAME"));
                messageArray.add(newMessage);
            }
            ;
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageArray;
    }

    public static void putMessage(Message message) {
        String query = "INSERT INTO MESSAGE_TABLE\n" +
                "           ([TOPIC]\n" +
                "           ,[MESSAGES]\n" +
                "           ,[TIME]\n" +
                "           ,[USERNAME])\n" +
                "     VALUES\n" +
                "           ('<TOPIC>'\n" +
                "           ,'<MESSAGES>'\n" +
                "           ,<TIME>\n" +
                "           ,'<USERNAME>')";

        query = query.replace("<TOPIC>", message.getTopic());
        query = query.replace("<MESSAGES>", message.getMessage());
        query = query.replace("<TIME>", message.getTime() + "");
        query = query.replace("<USERNAME>", message.getUsername());

        executeJdbcQuery(query);
    }

    public static boolean registerNewUser(String user, String password) {
        String query = "INSERT INTO [dbo].[USER_TABLE]\n" +
                "           ([USERNAME]\n" +
                "           ,[PASSWORD]\n" +
                "           ,[ACTIVE])\n" +
                "     VALUES\n" +
                "           ('<USERNAME>'\n" +
                "           ,'<PASSWORD>'\n" +
                "           ,<ACTIVE>)";
        query = query.replace("<USERNAME>", user);
        query = query.replace("<PASSWORD>", password);
        query = query.replace("<ACTIVE>", "1");

        executeJdbcQuery(query);
        return true;
    }

    public static double executeJdbcSingleOutputQuery(String query) {
        Connection con = ConnectionUtils.getConnection();
        Double toReturn = 0d;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                toReturn = rs.getDouble("VALUE");
            }
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static String convertToJsonArray(ArrayList<Message> messages) {
        String toReturn = "[";
        for (int i = 0; i < messages.size(); i++) {
            toReturn += covertToJson(messages.get(i));
            if(i != messages.size()-1)
                toReturn += ",";
        }
        toReturn += "]";
        return toReturn;
    }

    public static String covertToJson(Message message) {
        String toReturn = "{\"user\":\"<User>\", \"message\":\"<Message>\", \"time\":\"<Time>\"}";
        java.util.Date date = new java.util.Date(message.getTime()*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy | hh:mm aaa");
        toReturn = toReturn.replace("<User>", message.getUsername().replaceAll(" ", ""));
        toReturn = toReturn.replace("<Message>", message.getMessage());
        toReturn = toReturn.replace("<Time>", sdf.format(date));
        return toReturn;
    }
}
