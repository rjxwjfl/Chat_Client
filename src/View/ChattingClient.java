package src.View;


import src.Controller.Repository.SocketCorrespModule;
import src.Controller.ServerConnection;
import src.View.Enhancer.EnhancedBorderLine;
import src.View.Enhancer.EnhancedButton;
import src.View.Enhancer.EnhancedTextField;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Objects;

import javax.swing.*;
import javax.swing.border.Border;


//@Getter
public class ChattingClient extends JFrame {


    private static ChattingClient instance;

    public static ChattingClient getinstance() {
        if (instance == null) {
            instance = new ChattingClient();
        }
        return instance;
    }

    private final CardLayout mainCard;
    private final JPanel mainPanel;

    private final JList<String> cpChatList;
    private final DefaultListModel<String> userListModel;

    private final JTextArea rpContentsView;
    private final JLabel rpChatTitle;
    private static SocketCorrespModule socketCorrespModule;
    private String currentTitle;
    private final EnhancedTextField nickInputField;
    private final EnhancedTextField inputField;


    public ChattingClient() {
//		<< INIT >>

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "종료하시겠습니까?", "프로그램 종료",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    if (socketCorrespModule != null){
                        socketCorrespModule.stop();
                    }
                    System.out.println("Select : " + confirmed);
                    dispose();
                    System.exit(0);
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        setBounds(100, 100, 480, 800);
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 204, 0));
        mainPanel.setBorder(null);

        setContentPane(mainPanel);
        mainCard = new CardLayout();
        mainPanel.setLayout(mainCard);


//		<< CONNECTION >>


        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(255, 204, 0));
        mainPanel.add(loginPanel, "loginPanel");
        loginPanel.setLayout(null);

        nickInputField = new EnhancedTextField();
        Insets nifInset = new Insets(8,12,8,12);
        Border borderPadding = BorderFactory.createEmptyBorder(8,12,8,12);
        nickInputField.setMargin(nifInset);
        nickInputField.setHorizontalAlignment(SwingConstants.LEFT);
        nickInputField.setBorder(borderPadding);
        nickInputField.setBounds(73,495,300,45);
        nickInputField.setColumns(10);
        nickInputField.setOpaque(false);
        nickInputField.setFont(new Font("Droid Sans Mono Slashed", Font.PLAIN, 20));
        nickInputField.setArcHeight(10);
        nickInputField.setArcWidth(10);
        nickInputField.setHintText("Input your nickname.");
        loginPanel.add(nickInputField);

        JButton lpLoginBtn = new JButton("");
        lpLoginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loginSession();
            }
        });

        nickInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginSession();
                }
            }
        });

        lpLoginBtn.setIcon(new ImageIcon(Objects.requireNonNull(ChattingClient.class.getResource("../assets/login_med.png"))));
        lpLoginBtn.setBounds(73, 573, 300, 45);
        lpLoginBtn.setBackground(Color.white);
        lpLoginBtn.setOpaque(false);//투명하게
        lpLoginBtn.setBorderPainted(false);
        loginPanel.add(lpLoginBtn);

        JLabel lpImageIcon = new JLabel("");
        lpImageIcon.setIcon(new ImageIcon(Objects.requireNonNull(ChattingClient.class.getResource("../assets/kakaotalk_sharing_btn_medium_ov.png"))));
        lpImageIcon.setBounds(183, 226, 68, 69);
        loginPanel.add(lpImageIcon);


//		<< ChatList >>

        JPanel chatListPanel = new JPanel();
        chatListPanel.setBackground(new Color(255, 204, 0));
        mainPanel.add(chatListPanel, "chatListPanel");
        chatListPanel.setLayout(null);

        JScrollPane cpChatListScroll = new JScrollPane();
        cpChatListScroll.setBounds(133, 5, 326, 729);
        cpChatListScroll.setViewportView(null);
        cpChatListScroll.getViewport().setOpaque(false);
        cpChatListScroll.setBorder(null);
        chatListPanel.add(cpChatListScroll);


        userListModel = new DefaultListModel<>();
        cpChatList = new JList<>(userListModel);
        cpChatList.setBorder(new EnhancedBorderLine(Color.ORANGE, 6, 10, 10));
        cpChatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cpChatList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = cpChatList.locationToIndex(e.getPoint());
                    if (index != -1 && !userListModel.isEmpty()) {
                        currentTitle = userListModel.get(index);
                        rpChatTitle.setText(currentTitle);
                        socketCorrespModule.outputHandler(3, currentTitle);
                    }
                }
            }
        });

        cpChatList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int index = cpChatList.getSelectedIndex();
                    if (index != -1 && !userListModel.isEmpty()) {
                        String selectedTitle = userListModel.get(index);
                        rpChatTitle.setText(selectedTitle);
                        socketCorrespModule.outputHandler(3, selectedTitle);
                    }
                }
            }
        });



        cpChatListScroll.setViewportView(cpChatList);

        JLabel cpImageIcon = new JLabel("");
        cpImageIcon.setIcon(new ImageIcon(Objects.requireNonNull(ChattingClient.class.getResource("../assets/kakaotalk_sharing_btn_medium_ov.png"))));
        cpImageIcon.setBounds(33, 33, 70, 70);
        chatListPanel.add(cpImageIcon);

        EnhancedButton addBtn = new EnhancedButton();
        EnhancedBorderLine borderLine = new EnhancedBorderLine(Color.black, 2, 10, 10);
        addBtn.setBorder(borderLine);
        addBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                currentTitle = JOptionPane.showInputDialog(null,
                        "방제를 입력하세요.",
                        "생성",
                        JOptionPane.INFORMATION_MESSAGE);
                if (currentTitle != null) {
                    socketCorrespModule.outputHandler(5, currentTitle);
                } else {
                    JOptionPane.getRootFrame().dispose();
                }


            }
        });

        addBtn.setIcon(new ImageIcon(Objects.requireNonNull(ChattingClient.class.getResource("../assets/plus-round_icon-icons.com_50065.png"))));
        addBtn.setBounds(34, 666, 65, 65);
        addBtn.setBackground(Color.ORANGE);
        addBtn.setOpaque(false);
        addBtn.setBorderPainted(true);
        addBtn.setArcHeight(10);
        addBtn.setArcWidth(10);
        chatListPanel.add(addBtn);


//		<< ChatRoomPanel >>


        JPanel chatRoomPanel = new JPanel();
        chatRoomPanel.setBackground(new Color(255, 204, 0));
        mainPanel.add(chatRoomPanel, "chatRoomPanel");
        chatRoomPanel.setLayout(null);


        JScrollPane rpContentsScroll = new JScrollPane();
        rpContentsScroll.setBounds(7, 103, 450, 590);
        rpContentsScroll.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8), BorderFactory.createLineBorder(new Color(255, 255, 255, 0))));
        rpContentsScroll.setViewportBorder(BorderFactory.createEmptyBorder());
        rpContentsScroll.getViewport().setOpaque(true);
        chatRoomPanel.add(rpContentsScroll);

        rpContentsView = new JTextArea();
//        rpContentsView.setBorder(new LineBorder(new Color(255, 255, 255, 0), 0));
        rpContentsScroll.setViewportView(rpContentsView);

        inputField = new EnhancedTextField();
        inputField.setBounds(7, 703, 366, 50);
        inputField.setHintText("Input your message...");
        inputField.setMargin(nifInset);
        inputField.setBorder(borderPadding);
        inputField.setArcHeight(10);
        inputField.setArcWidth(10);
        inputField.setBackground(Color.white);
        chatRoomPanel.add(inputField);

        JButton rpChatOutBtn = new JButton("");
        rpChatOutBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearAll();
                socketCorrespModule.outputHandler(4, currentTitle);
            }
        });

        rpChatOutBtn.setIcon(new ImageIcon(Objects.requireNonNull(ChattingClient.class.getResource("../assets/quit_icon_149882.png"))));
        rpChatOutBtn.setBounds(390, 29, 45, 50);
        rpChatOutBtn.setBackground(Color.ORANGE);
        rpChatOutBtn.setBorder(new EnhancedBorderLine(Color.gray, 2, 10, 10));
        rpChatOutBtn.setFocusPainted(false);
        rpChatOutBtn.setContentAreaFilled(false);
        chatRoomPanel.add(rpChatOutBtn);


        rpChatTitle = new JLabel("");
        rpChatTitle.setBounds(30, 29, 300, 46);
        rpChatTitle.setFont(new Font("Droid Sans Mono Slashed", Font.BOLD, 32));
        chatRoomPanel.add(rpChatTitle);


        JButton rpInputSubmit = new JButton("");
        rpInputSubmit.setIcon(new ImageIcon(Objects.requireNonNull(ChattingClient.class.getResource("../assets/mail_send_icon_180871.png"))));
        rpInputSubmit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sMsgInd();
            }
        });
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sMsgInd();
                }
            }
        });
        rpInputSubmit.setBounds(382, 703, 75, 50);
        rpInputSubmit.setBorderPainted(false);
        rpInputSubmit.setContentAreaFilled(false);
        rpInputSubmit.setFocusPainted(false);
        chatRoomPanel.add(rpInputSubmit);
    }



    public void paneController(String pane) {
        mainCard.show(mainPanel, pane);
    }

    public void addChatMsd(String msg) {
        rpContentsView.append("\n" + msg);
    }

    public void createNewChat(String host, String title) {
        rpContentsView.append("\n[[    A new chat creation complete.    ]]\n");
        rpContentsView.append("\n[[    Chat Host : " + host + "    ]]\n");
        rpChatTitle.setText(title);
    }

    public void notifyMsg(String content) {
        JOptionPane.showMessageDialog(null,
                content,
                "ERROR",
                JOptionPane.ERROR_MESSAGE);
    }

    public void addItem(List<String> items) {
        userListModel.clear();
        for (String i : items) {
            userListModel.addElement(i);
        }
    }

    public void loginSession(){
        ServerConnection serverConnection = new ServerConnection();
        if (nickInputField.getText().isBlank()) {
            notifyMsg("닉네임을 입력해주세요.");
            return;
        }
        socketCorrespModule = serverConnection.connect(nickInputField.getText());
    }

    public void sMsgInd(){
        String userNickName = socketCorrespModule.getUser().getNickName();
        if (!inputField.getText().isBlank()){
            rpContentsView.append("\n" + userNickName + " >> : " + inputField.getText());
            socketCorrespModule.outputHandler(6, inputField.getText());
            inputField.setText("");
        }
    }

    public void clearAll(){
        rpContentsView.selectAll();
        rpContentsView.replaceSelection("");
        userListModel.clear();
    }

}
