package com.example.demo.webSocket;

import com.example.demo.utils.UserUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{username}")
public class WebSocket {
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    /**
     *  在线人数
     */
    public static int onlineNumber=0;

    /**
     *  以用户名为key，websocket为对象保存起来
     */
    private static Map<String, WebSocket> clients=new ConcurrentHashMap<String, WebSocket>();

    /**
     *  会话
     */
    private Session session;

    /**
     *  用户名
     */
    private String username;

    private static Gson gson=new Gson();

    @OnOpen
    public void open(@PathParam("username") String username,Session session) throws IOException {
        onlineNumber++;
        logger.info("现在来连接的用户id："+session.getId()+"用户名："+username);
        this.session=session;
        this.username=username;
        logger.info("有新连接加入，当前在线人数："+onlineNumber);
        try {
            //messageType 1代表上线，2代表下线，3代表在线名单，4代表普通消息
            //先发给所有人，告诉他们我上线了
            Map<String,Object> map1=new HashMap<>();
            map1.put("messageType",1);
            map1.put("username",username);
            sendMessageAll(gson.toJson(map1),username);

            //把自己的信息加到map中
            clients.put(username,this);
            System.out.println("this的内容是："+this);
            //给自己发送一条信息，告诉自己现在都有谁在线
            Map<String,Object> map2=new HashMap<>();
            map2.put("messageType",3);
            //移除掉自己
            Set<String> set=clients.keySet();
            for (String key:clients.keySet()) {
                System.out.println("clients.keyset是什么？"+clients.keySet());
            }
            map2.put("onlineUsers",set);
            sendMessageTo(gson.toJson(map2),username);
        }catch (IOException e){
            logger.info(username+"上线通知发生错误");
        }
    }

    @OnError
    public void onError(Session session,Throwable error){
        logger.info("服务端发生错误"+error.getMessage());
    }

    @OnClose
    public void onClose() throws IOException {
        onlineNumber--;
        clients.remove(username);
        try {
            Map<String,Object> map=new HashMap<>();
            map.put("messageType",2);
            map.put("onlineUsers",clients.keySet());
            map.put("username",username);
            sendMessageAll(gson.toJson(map),username);
        }catch (IOException e){
            logger.info(username+"下线时发生了错误！");
        }
        logger.info("有关闭连接，当前人数："+onlineNumber);
    }

    @OnMessage
    public String onMessage(String message,Session session) throws IOException {
        try {
            logger.info("来自客户端的消息是"+message+"客户端id是："+session.getId());
            JsonObject jsonObject=(JsonObject)new JsonParser().parse(message);
            String textMessage=jsonObject.get("message").getAsString();
            String fromUsername=jsonObject.get("username").getAsString();
            String toUsername=jsonObject.get("to").getAsString();
            //如果不是发给所有人，就是发给一个人
            Map<String,Object> map=new HashMap<>();
            map.put("messageType",4);
            map.put("textMessage",textMessage);
            map.put("fromUsername",fromUsername);
            if (toUsername.equals("All")){
                map.put("toUsername","所有人");
                sendMessageAll(gson.toJson(map),fromUsername);
            }else{
                int userId= UserUtil.getId(fromUsername);
                int receive= UserUtil.getId(toUsername);
                String status= UserUtil.getStatus(userId,receive);
                if (status.equals("1")) {
                    //此处调用一个验证用户之间是否是好友的方法
                    map.put("toUsername", toUsername);
                    System.out.println("向某个人发送信息！");
                    sendMessageTo(gson.toJson(map), fromUsername);
                }else{
                    System.out.println("请先加好友！");
                    return "请先添加对对方为好友!";
                }
            }
        }catch (Exception e){
            logger.info("客户端消息发生了错误！"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void sendMessageTo(String message,String toUserName) throws IOException {
        for (WebSocket item:clients.values()){
            if (item.username.equals(toUserName)){
                item.session.getAsyncRemote().sendText(message);
                break;
            }
        }
    }

    public void sendMessageAll(String message,String fromUserName) throws IOException{
        for (WebSocket item:clients.values()){
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineNumber(){
        return onlineNumber;
    }

    /*@RequestMapping("/webSocket/{name}")
    public void webSocket(@PathVariable String username, Model model){
        try {
            logger.info("跳转到websocket页面");
            model.addAttribute("username",username);
        }catch (Exception e){
            logger.info("跳转到websocket页面发生异常；异常信息是："+e.getMessage());
        }
    }*/
}
