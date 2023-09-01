import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {


    static ArrayList<String>userName= new ArrayList<String>();
    static ArrayList<PrintWriter>printWriters= new ArrayList<PrintWriter>();


    public static void main(String[] args) throws Exception {
        System.out.println("waiting for client...");
        ServerSocket ss= new ServerSocket(3200);
        while(true){
            Socket soc= ss.accept();
            System.out.println("connection established");
            ConversationHandler handler = new ConversationHandler(soc);
            handler.start();
        }
    }
}
class ConversationHandler extends Thread{
    Socket socket;
    BufferedReader in;
    PrintWriter out; // will be used for socket writing
    String name;
    PrintWriter pw;//will be used writing data into text file
    static FileWriter fw; // will write character by character to file
    static BufferedWriter bw;
    public ConversationHandler(Socket socket) throws Exception{
        this.socket=socket;
        fw = new FileWriter("ChatServer-logs.txt",true);
        bw = new BufferedWriter(fw);
        pw = new PrintWriter(bw,true);
    }
    public void run(){
        try{
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            int count=0;
            while (true){
                if(count>0){
                    out.println("NAMEALREADYEXIST");
                }
                else{
                    out.println("NAMEREQUIRED");
                }
                name=in.readLine();
                if(name==null){
                    return;
                }
                if(!ChatServer.userName.contains(name)){
                    ChatServer.userName.add(name);
                    break;
                }
                count++;
            }
            out.println("NAMEACCEPTED"+name);
            ChatServer.printWriters.add(out);

            // this while loop reads the  msg coming from client and distribute the msg to all the clients
            while (true){
                String message = in.readLine();
                if(message == null){
                    return;
                }
               // System.out.println(message);
                pw.println(name + ": "+message);
                for(PrintWriter writer:ChatServer.printWriters){
                    writer.println(name+ ": "+ message);
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}