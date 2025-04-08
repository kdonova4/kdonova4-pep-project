package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    /**
     * Creates a message using MessageDAO
     * @param message
     * @return message created
     */
    public Message createMessage(Message message) {
        if(validate(message)) {
            return messageDAO.createMessage(message);
        } else {
            return null;
        }
    }

    /**
     * finds all messages using MessageDAO
     * @return all messages
     */
    public List<Message> findAll() {
        return messageDAO.findAll();
    }

    /**
     * finds a message by it's ID
     * @param id
     * @return the message found
     */
    public Message findMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    /**
     * delete a message by its ID
     * @param id
     * @return the message deleted
     */
    public Message deleteMessageById(int id) {
        return messageDAO.deleteMessageById(id);
    }

    /**
     * updates a messages message_text field
     * @param id
     * @param message
     * @return the final updated message
     */
    public Message updateMessage(int id, Message message) {
        if(validate(id, message)) {
            if(messageDAO.updateMessage(id, message)) {
                return messageDAO.getMessageById(id);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * finds all messages from a user by their ID
     * @param userId
     * @return the list of messages by user
     */
    public List<Message> getMessagesFromUserById(int userId) {
        return messageDAO.getAllMessagesFromUserById(userId);
    }

    /**
     * validates for update
     * checks if text is blank
     * checks if text is too big
     * checks if id has any message attached to it
     * @param id
     * @param message
     * @return true if update is valid to continue, false if not
     */
    private boolean validate(int id, Message message) {
        if(message.getMessage_text().isBlank()) {
            return false;
        }

        if(message.getMessage_text().length() >= 255) {
            return false;
        }

        if(messageDAO.getMessageById(id) == null) {
            return false;
        }

        return true;
    }

    /**
     * validates a message
     * @param message
     * @return true if the message is valid, false if not
     */
    private boolean validate(Message message) {
        if(message == null) {
            return false;
        }

        if(message.getMessage_text().isBlank()) {
            return false;
        }

        if(message.getMessage_text().length() >= 255) {
            return false;
        }

        if(accountDAO.findByAccountId(message.getPosted_by()) == null) {
            return false;
        }

        return true;
    }

}
