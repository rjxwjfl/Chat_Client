package src;

import View.ChattingClient;

import java.awt.*;

public class ClientMain {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ChattingClient frame = ChattingClient.getinstance();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
