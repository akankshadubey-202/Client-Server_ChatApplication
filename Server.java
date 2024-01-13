import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.io.*;

public class Server extends JFrame{
    //constructor
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    //Declaration of Components
    private JLabel heading =new JLabel("Server side");
    private JTextArea messageArea =new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    //constructor
    public Server()
    {  try {
        server = new ServerSocket(7778);
        System.out.println("Server is ready to accept connection");
        System.out.println("Waiting....");
        socket= server.accept();
       br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
       out=new PrintWriter(socket.getOutputStream());
        createGUI();
        handleEvents();

        startReading();
       //startWriting();
    }catch (Exception e)
    {
        System.out.println("Error!!");
    }

    }
//create GUI
    public void createGUI()
    {
        this.setTitle("Server Message[END]");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("servericon.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));


        //message
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        //set the layout for frame
        this.setLayout(new BorderLayout());
        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void handleEvents()
    {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("key released "+e.getKeyCode());
                if(e.getKeyCode()==10)
                {
                    String contentToSend=messageInput.getText();
                    messageArea.append("Server : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }
            }
        });
    }
    public void startReading() {
        //thread  will read and give output
        Runnable r1 = () -> {
            System.out.println("reader started...");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        JOptionPane.showMessageDialog(this,"Client Terminated the Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                    messageArea.append("Client: " + msg+"\n");
                }
            }catch (Exception e) {
                System.out.println("Connection closed");
            }
        };
        new Thread(r1).start();

    }
   public void startWriting()
    {
     //thread -data will be taken from user and then send to client
        Runnable r2=()->{
            System.out.println("writer Started.... ");
   try {
       while (!socket.isClosed()){

           BufferedReader br1 = new BufferedReader((new InputStreamReader(System.in)));
           String content = br1.readLine();
           out.println(content);
           out.flush();
           if(content.equals("exit"))
           {
               socket.close();
               break;
           }

       }
   }
       catch(Exception e)
       {
           System.out.println("Connection closed");
       }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args)
    {
        System.out.println("this is server...going to start the server ");
        new Server();
    }

}
