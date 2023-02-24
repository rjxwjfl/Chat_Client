package Controller.Repository;

import View.ChattingClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import Model.DataTransferObject.Dto;
import Model.MFSmodel;
import Model.MTSmodel;
import Model.UserModel;
import Controller.Thread.InputThread;
import Controller.Thread.Interface.InputThreadListener;
import Controller.Thread.Interface.OutputThreadListener;
import Controller.Thread.OutputThread;

import java.io.IOException;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class SocketCorrespModule implements InputThreadListener, OutputThreadListener {
    private final Socket socket;
    private final Gson gson;
    private final InputThread in;
    private final OutputThread out;
    private final PrintWriter pw;
    private UserModel user;
    private Dto inputDto;
    private String currentTitle;
    private List<String> list;

    public SocketCorrespModule(Socket socket) throws IOException {
        this.socket = socket;
        this.gson = new GsonBuilder().setLenient().create();

        in = new InputThread(socket.getInputStream(), this);
        out = new OutputThread(this);
        pw = new PrintWriter(socket.getOutputStream());

    }

    public void connectionSwitch(boolean isConnect) {
        if (isConnect) {
            start();
        } else {
            stop();
        }
    }

    public void start() {
        in.start();
        out.start();
    }

    public void stop() {
        in.interrupt();
        out.interrupt();
        currentTitle = null;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inputHandler(Dto<?> dto) {
        int code = dto.getTransferCode();
        ChattingClient cc = ChattingClient.getinstance();

        switch (code) {
            case 1: // Connection complete. Loading entire chat list.
                break;
            case 2: // Set UserModel
                LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) dto.getBody();
                user = new UserModel((String) map.get("nickName"), ((Double) map.get("PORT")).intValue());
                cc.paneController("chatListPanel");
                break;
            case 3: // Chat room entry complete.
                cc.paneController("chatRoomPanel");
                double initEntry = (double) dto.getBody();
                cc.userNums((int) initEntry);
                break;
            case 4: // Left Complete
                list = (List<String>) dto.getBody();
                cc.addItem(list);
                cc.paneController("chatListPanel");
                break;
            case 5: // A new chat creation complete
                cc.userNums(1);
                cc.paneController("chatRoomPanel");
                cc.createNewChat(user.getNickName(), currentTitle);
                break;
            case 7: // A Message reception
                LinkedTreeMap<?, ?> msg = (LinkedTreeMap<?, ?>) dto.getBody();
                UserModel sender = new UserModel((String) ((LinkedTreeMap<?, ?>) msg.get("sender")).get("nickName"),
                        ((Double) ((LinkedTreeMap<?, ?>) msg.get("sender")).get("PORT")).intValue());
                MFSmodel cht = new MFSmodel(sender, (String) msg.get("contents"));
                cc.addChatMsd("[ " + cht.getSender().getNickName() + " ]  :  " + cht.getContents());
                break;
            case 101: // A new chat has been added. / A chat has been removed.
                list = (List<String>) dto.getBody();
                cc.addItem(list);
                break;
            case 201: // A new user entered this chat.
                outputHandler(401, "request");
                String uIn = (String) dto.getBody();
                cc.addChatMsd("<< SYSTEM MESSAGE >>\n    " + uIn + " 님이 접속하셨습니다.");
                break;
            case 202: // A user left this chat.
                outputHandler(401, "request");
                String uOut = (String) dto.getBody();
                cc.addChatMsd("<< SYSTEM MESSAGE >>\n    " + uOut + " 님이 퇴장하셨습니다.");
                break;
            case 301: // The host has left.
                cc.clearAll();
                cc.notifyMsg("The Host has left");
                cc.paneController("chatListPanel");
                list = (List<String>) dto.getBody();
                cc.addItem(list);
                break;
            case 401: // Update the number of people in the chat room
                double body = (double) dto.getBody();
                cc.userNums((int) body);
                break;
        }
    }

    public void outputHandler(int code, Object body) {
        Dto<?> dto;
        switch (code) {
            case 1: // Connection request.
                dto = new Dto<>(code, (String) body);
                System.out.println(dto);
                onOutput(dto);
                break;
            case 3: // Send a request for enter the chat room.
            case 401:
                dto = new Dto<>(code, body);
                onOutput(dto);
                break;
            case 4: // Left notify to server.
                dto = new Dto<>(4, body);
                onOutput(dto);
                break;
            case 5: // Create a new chat.
                dto = new Dto<>(code, body);
                currentTitle = (String) body;
                onOutput(dto);
                break;
            case 6: // A Message sending.
                String msg = (String) body;
                MTSmodel ts = new MTSmodel(user, msg, false, null);
                dto = new Dto<>(code, ts);
                onOutput(dto);
                break;
        }
    }

    @Override
    public void onInput(String msg) {
        String[] msgList = msg.split("\\r?\\n");
        for (String str : msgList) {
            try {
                inputDto = gson.fromJson(str, Dto.class);
                if (inputDto != null) {
                    inputHandler(inputDto);
                }
                return;
            } catch (JsonSyntaxException e) {
                System.out.println("!! ERROR !!\nDETAILS : " + e);
            }
        }
    }

    @Override
    public void onOutput(Dto<?> dto) {
        out.addPrintWriter(pw);
        out.onSendingMessage(gson.toJson(dto));
    }

    @Override
    public void onConnectionLost() {
        connectionSwitch(false);
    }

    public UserModel getUser() {
        return user;
    }
}
