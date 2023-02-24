package Controller.Thread;

import View.ChattingClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class EchoThread extends Thread{
    private static final long DELAYED = 10000;
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public EchoThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            while (true){
                byte[] echoBuffer = new byte[1024];
                int n = in.read(echoBuffer);
                if ("ALIVE".equals(new String(echoBuffer, 0, n).trim())){
                    Thread.sleep(DELAYED);
                    out.write("ALIVE\n".getBytes());
                    out.flush();
                }
            }
        } catch (SocketException e){
            System.out.println("Connection to the server has been forced to terminate.");
            ChattingClient.getinstance().forcedExit();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
