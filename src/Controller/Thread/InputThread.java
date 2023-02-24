package Controller.Thread;

import Controller.Thread.Interface.InputThreadListener;
import View.ChattingClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

public class InputThread extends Thread{
    private InputStream inputStream;
    private InputThreadListener listener;

    public InputThread(InputStream in, InputThreadListener listener) {
        this.inputStream = in;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            while (true){
                byte[] buffer = new byte[1024];
                int a = inputStream.read(buffer);
                String inputString = new String(buffer, 0, a).trim();
                while (a == buffer.length) {
                    a = inputStream.read(buffer);
                    inputString += new String(buffer, 0, a).trim();
                }
                String[] inputs = inputString.split("\n");
                for (String input : inputs){
                    listener.onInput(input);
                }
            }
        } catch (SocketException e){
            System.out.println("!! ERROR !!\nDETAILS : " + e);
            listener.onConnectionLost();
            ChattingClient.getinstance().paneController("loginPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
