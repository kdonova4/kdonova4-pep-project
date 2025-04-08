package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("register", this::createAccountHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::createMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.get("accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);
        app.patch("messages/{message_id}", this::updateMessageHandler);
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        return app;
    }

    /**
     * handles creating accounts at 'POST localhost:8080/register'
     * 
     * Expected Status Codes:
     * - 200 OK: Account successfully created and returned in the response body.
     * - 400 Bad Request: Account creation failed due to validation.
     *
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void createAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount != null) {
            context.json(mapper.writeValueAsString(addedAccount));
        } else {
            context.status(400);
        }
    }

    /**
     * handles logging into accounts at 'POST localhost:8080/login'
     * 
     * Expected Status Codes:
     * - 200 OK: Account successfully logged into and returned in the response body.
     * - 401 Unauthorized: Account login failed due to validation.
     *
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        System.out.println("USERNAME: " + account.getUsername());
        System.out.println("PASSWORD: " + account.getPassword());
        Account loggedAccount = accountService.login(account);
        if(loggedAccount != null) {
            context.json(mapper.writeValueAsString(loggedAccount));
            context.status(200);
        } else {
            context.status(401);
        }
    }

    /**
     * handles creating messages 'POST localhost:8080/messages'
     * 
     * Expected Status Codes:
     * - 200 OK: Message succesfully created and returned in the response body.
     * - 400 Bad Request: Message creation failed due to validation.
     *
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void createMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        if(createdMessage != null) {
            context.json(mapper.writeValueAsString(createdMessage));
        } else {
            context.status(400);
        }
    }

    /**
     * handles fetching all messages 'GET localhost:8080/messages'
     * 
     * Expected Status Codes:
     * - 200 OK: Messages list returned in the response body.
     *
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.findAll();
        context.json(messages);
    }

    /**
     * handles fetching a message by ID 'GET localhost:8080/messages/{message_id}'
     * 
     * Expected Status Codes:
     * - 200 OK: Message found/not found.
     *
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void getMessageByIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(context.pathParam("message_id"));

        Message message = messageService.findMessageById(id);

        if(message != null) {
            context.json(mapper.writeValueAsString(message));
        } else {
            context.status(200);
        }
    }

    /**
     * handles fetching all messages by user 'GET localhost:8080/accounts/{account_id}/messages'
     * 
     * Expected Status Codes:
     * - 200 OK: Messages list returned in the response body.
     *
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessagesByAccountIdHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("account_id"));

        List<Message> messages = messageService.getMessagesFromUserById(id);

        context.json(messages);
    }

    /**
     * handles updating a messages message_text field by ID 'PATCH localhost:8080/messages/{message_id}'
     * 
     * Expected Status Codes:
     * - 200 OK: update was successful.
     * - 400 Bad Request: Message update not succesful.
     *
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void updateMessageHandler(Context context) throws JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("message_id"));

        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message updatedMessage = messageService.updateMessage(id, message);

        if(updatedMessage != null) {
            context.json(mapper.writeValueAsString(updatedMessage));
        } else {
            context.status(400);
        }
    }

    /**
     * handles deleting a message by ID 'DELETE localhost:8080/messages/{message_id}'
     * 
     * Expected Status Codes:
     * - 200 OK: delete successful/message not found
     *
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void deleteMessageHandler(Context context) throws JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("message_id"));

        Message message = messageService.deleteMessageById(id);

        if(message != null) {
            context.json(message);
        } else {
            context.status(200);
        }
    }



}