import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    SymmetricEn symmetricEn;

    static JFrame chatWindow = new JFrame("Chat Application");
    static JTextArea chatArea = new JTextArea(22,40);
    static JTextField textField = new JTextField(40);
    static JLabel blindTable = new JLabel("      ");
    static JButton sendButton = new JButton("Send");
    static BufferedReader in;
    static PrintWriter out;
    static JLabel nameLabel= new JLabel("          ");
    ChatClient(){
        chatWindow.setLayout(new FlowLayout());
        chatWindow.add(new JScrollPane(chatArea));
        chatWindow.add(blindTable);
        chatWindow.add(textField);
        chatWindow.add(sendButton);

        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.setSize(475,500);
        chatWindow.setVisible(true);

        textField.setEditable(false);
        chatArea.setEditable(false);

        sendButton.addActionListener(new Listener());
        textField.addActionListener(new Listener());

        symmetricEn = new SymmetricEn();
    }

    void startChat() throws Exception{
        String ipAddress = JOptionPane.showInputDialog(
                chatWindow,
                "Enter IP Address:",
                "IP Address Required!!",
                JOptionPane.PLAIN_MESSAGE);
        Socket soc = new Socket(ipAddress,3200);
        in= new BufferedReader(new InputStreamReader(soc.getInputStream()));
        out=new PrintWriter(soc.getOutputStream(),true);
        while (true){
            String str=in.readLine();

           // System.out.println(str);

            if(str.equals("NAMEREQUIRED")){
                String name = JOptionPane.showInputDialog(
                        chatWindow,
                        "Enter a unique name:",
                        "Name Required!!",
                        JOptionPane.PLAIN_MESSAGE);
                out.println(name);
            }
            else if(str.equals("NAMEALREADYEXIST")){
                String name = JOptionPane.showInputDialog(
                        chatWindow,
                        "Enter another name:",
                        "Name Already Exist!!",
                        JOptionPane.WARNING_MESSAGE);
                out.println(name);
            }
            else if(str.startsWith("NAMEACCEPTED")){
                textField.setEditable(true);
                nameLabel.setText("You are logged in as: "+str.substring(12));
            }
            else{
                chatArea.append(str + "\n");
            }
        }
    }
    public static void main(String[] args) throws Exception {
        ChatClient client=new ChatClient();
        client.startChat();
    }
}

class Listener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        ChatClient.out.println(ChatClient.textField.getText()); // sending msg to the server
      //  System.out.println(ChatClient.textField.getText());
        ChatClient.textField.setText("");
    }
}