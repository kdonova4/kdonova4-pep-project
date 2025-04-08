package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    /**
     * Create a message
     * @param message
     * @return Message created
     */
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkResultSet = preparedStatement.getGeneratedKeys();
            if(pkResultSet.next()) {
                int generated_message_id = (int) pkResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Finds all messages
     * @return all messages
     */
    public List<Message> findAll() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "select * from message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    
    /**
     * finds a message by its ID
     * @param id
     * @return the message found or null if not found
     */
    public Message getMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "select * from message where message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                    );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    
    /**
     * Delete a message by its ID
     * @param id
     * @return The message deleted
     */
    public Message deleteMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage = null;
        try {
            // first find the message to return it later
            String sqlFind = "select * from message where message_id = ?;";
            PreparedStatement selectPreparedStatement = connection.prepareStatement(sqlFind);
            selectPreparedStatement.setInt(1, id);
            ResultSet rs = selectPreparedStatement.executeQuery();

            if(rs.next()) {
                deletedMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                    );

                // now that we have found the message, we can delete it.
                String sqlDelete = "delete from message where message_id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete);
        
                preparedStatement.setInt(1, id);
        
                preparedStatement.executeUpdate(); // delete
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return deletedMessage;
    }

    
    /**
     * Updates the message_text of the Message
     * @param id - message id
     * @param message 
     * @return true if update completed, false if not
     */
    public boolean updateMessage(int id, Message message) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "update message set message_text = ? where message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, id);

            // did it update any rows?
            return preparedStatement.executeUpdate() > 0; 
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * finds all messages posted by a user by their ID
     * @param userId
     * @return List of messages by a user
     */
    public List<Message> getAllMessagesFromUserById(int userId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "select * from message where posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
        

}
