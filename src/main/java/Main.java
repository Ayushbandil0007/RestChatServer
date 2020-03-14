import dbhandler.ConnectionUtils;
import dbhandler.Message;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.JSONListAdapter;
import org.webbitserver.*;
import org.webbitserver.netty.NettyWebServer;
import org.apache.commons.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        WebServer webServer = new NettyWebServer(9992);
        final Rest rest = new Rest(webServer);
        rest.GET("/login", new LoginUser());
        rest.GET("/topics", new ChatTopics());
        rest.GET("/chatroom/{topic}", new ChatRoom());
        rest.GET("/register", new RegisterUser());
        rest.GET("/static/*", new HttpHandler() {
            @Override
            public void handleHttpRequest(HttpRequest req, HttpResponse res, HttpControl ctl) {
                try{
                    String filepath = req.uri().replace("/static/", "");
                    InputStream inputStream = getClass()
                            .getClassLoader().getResourceAsStream(filepath);
                    byte[] byteArr = IOUtils.toByteArray(inputStream);

                    res.content(byteArr).end();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });

        rest.POST("/register", new HttpHandler() {
            @Override
            public void handleHttpRequest(HttpRequest req, HttpResponse res, HttpControl ctl) {
                String username = req.postParam("username");
                String password = req.postParam("password");
                ConnectionUtils.registerNewUser(username, password);
                res.content("{\"code\":\"200\",\"status\":\"success\"}").end();
            }
        });

        rest.POST("/authenticate", new HttpHandler() {
            @Override
            public void handleHttpRequest(HttpRequest req, HttpResponse res, HttpControl ctl) {
            String username = req.postParam("username");
            String password = req.postParam("password");

            boolean isValidUser = ConnectionUtils.isValidUser(username, password);
                if(isValidUser){
                    res.content("{\"code\":\"200\",\"status\":\"valid\"}").end();
                } else{
                    res.content("{\"code\":\"200\",\"status\":\"invalid\"}").end();
                }
            }
        });

        rest.GET("/getMessages/{topic}", new HttpHandler() {
            @Override
            public void handleHttpRequest(HttpRequest req, HttpResponse res, HttpControl ctl) {
                String topic = req.uri().split("/")[2];
                String json = ConnectionUtils.convertToJsonArray(ConnectionUtils.getAllMessages(topic));
                res.content(json).end();
            }
        });

        rest.PUT("/sendMessage", new HttpHandler() {
            @Override
            public void handleHttpRequest(HttpRequest req, HttpResponse res, HttpControl ctl) {
                String message = req.postParam("message");
                String user = req.postParam("user");
                String url = req.postParam("url");
                String topic = url.split("/")[4];
                ConnectionUtils.putMessage(new Message(topic, message, (int) (new Date().getTime() / 1000), user));
                res.content("{\"code\":\"200\",\"status\":\"success\"}").end();
            }
        });

        webServer.start().get();
        System.out.println("Try this: curl -i localhost:9992/login");
    }
}
