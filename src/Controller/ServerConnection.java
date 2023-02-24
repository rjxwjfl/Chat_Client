package Controller;

import Controller.Repository.SocketCorrespModule;
import Controller.Thread.EchoThread;
import View.ChattingClient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnection {
    private static final String iNetAddress = "192.168.2.10";
    private static final int PORT_MAIN = 8080;
    private static final int PORT_SUB = 8888;
    private static SocketCorrespModule scm;
    private Socket socket;
    private Socket eSocket;

    public SocketCorrespModule connect(String input){
        try {
            socket = new Socket(iNetAddress, PORT_MAIN);
            eSocket = new Socket(iNetAddress, PORT_SUB);

            scm = new SocketCorrespModule(socket);
            EchoThread echoThread = new EchoThread(eSocket);

            echoThread.start();
            scm.connectionSwitch(true);

            scm.outputHandler(1, input);

        } catch (ConnectException e) {
            ChattingClient.getinstance().notifyMsg("서버와 연결할 수 없습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scm;
    }
}
