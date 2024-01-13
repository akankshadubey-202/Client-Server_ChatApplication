import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;


public class Client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;


    //Declaration of Components
    private JLabel heading =new JLabel("Client side");
    private JTextArea messageArea =new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);


//constructor
    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7778);
            System.out.println("Connection Done");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

                createGUI();
                handleEvents();

            startReading();
            //startWriting();
        } catch (Exception e) {
            System.out.println("Error!!");
        }
    }
    public void createGUI()
    {
        this.setTitle("Cilent Message[END]");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("clogo.webp"));
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
    messageArea.append("Me : "+contentToSend+"\n");
    out.println(contentToSend);
    out.flush();
    messageInput.setText("");
    messageInput.requestFocus();

}
            }
        });
    }
//start reading
    public void startReading() {
        // thread will read and give output
        Runnable r1 = () -> {
            System.out.println("reader started...");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this,"Server Terminated the Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                   // System.out.println("Server: " + msg);
                    messageArea.append("Server: " + msg+"\n");
                }
            } catch (Exception e) {
                System.out.println("Connection closed");
            }
        };
        new Thread(r1).start();
    }
//start writing
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

    public static void main(String[] args) {
        System.out.println("Hello Client");
        new Client();
    }
}
