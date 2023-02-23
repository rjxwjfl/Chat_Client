package src.Controller.Thread;

import src.Controller.Thread.Interface.InputThreadListener;
import src.View.ChattingClient;

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
                System.out.println("Waiting for input...");
                byte[] buffer = new byte[1024];
                int a = inputStream.read(buffer);
                String inputString = new String(buffer, 0, a).trim();
                while (a == buffer.length) {  // If buffer is fully filled, read again to get remaining input
                    a = inputStream.read(buffer);
                    inputString += new String(buffer, 0, a).trim();
                }
                String[] inputs = inputString.split("\n");
                for (String input : inputs){
                    listener.onInput(input);
                    System.out.println("New --- >" + input);
                }
            }
        } catch (SocketException e){
            System.out.println("!! ERROR !!\nDETAILS : " + e);
            ChattingClient.getinstance().paneController("loginPanel");
            listener.onConnectionLost();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
