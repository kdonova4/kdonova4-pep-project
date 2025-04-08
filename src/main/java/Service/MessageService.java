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

    public Message createMessage(Message message) {
        if(validate(message)) {
            return messageDAO.createMessage(message);
        } else {
            return null;
        }
    }

    public List<Message> findAll() {
        return messageDAO.findAll();
    }

    public Message findMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id) {
        return messageDAO.deleteMessageById(id);
    }

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

    public List<Message> getMessagesFromUserById(int userId) {
        return messageDAO.getAllMessagesFromUserById(userId);
    }

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
