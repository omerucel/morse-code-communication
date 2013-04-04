package com.omerucel.morse.code.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * İstemciler arasındaki iletişimi sağlayan sunucu uygulamasını içerisinde
 * barındırır. 9090 portunu dinler.
 * 
 * @author omer
 */
public class Server{

    // Singleton deseni için Server objesini tutar.
    private static Server instance;

    // 9090 portunu dinleyen socket.
    ServerSocket serverSocket;

    // Sunucuya bağlı bulunan istemci bilgilerini tutar.
    HashMap<String, ServerClient> clients = new HashMap<String, ServerClient>();

    /**
     * Singleton deseni için yardımcı metod.
     * 
     * @return 
     */
    public static Server getInstance()
    {
        if (instance == null)
            instance = new Server();

        return instance;
    }

    // Thread çalıştığında bu metod devreye girer.
    public void listen(int port)
    {
        try {
            // Sunucu socket ile 9090 portunu dinlemeye başlar.
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
            return;
        }

        try {

            // Socket açık kaldığı müddetçe gelen bağlantı isteklerine yanıt verir.
            while(!serverSocket.isClosed())
            {
                // Yeni gelen bağlantılarda istemci için yeni bir socket açılır.
                Socket socket = serverSocket.accept();
                // Bu socket ServerClient objesine aktarılır ve yeni bir thread
                // çalışmaya başlar.
                new Thread(new ServerClient(socket)).start();
            }
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }

        try {
            // Sunucu 9090 portunu dinlemeyi keser.
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    /**
     * Yeni bir istemci bağlandığında, ilgili istemci için üretilen 
     * ServerClient objesini clients isimli listeye aktarır.
     * 
     * @param client 
     */
    public synchronized void clientConnected(ServerClient client)
    {
        clients.put(client.toString(), client);
        notifyAll();
    }

    /**
     * Bir istemcinin sunucu ile olan bağlantısı koptuğunda, ilgili
     * listeden istemci id bilgisini siler.
     * 
     * @param client 
     */
    public synchronized void clientDisconnected(ServerClient client)
    {
        clients.remove(client.toString());
        notifyAll();
    }

    /**
     * Bir istemcinin, sunucuya bağlı olan tüm istemcilere mesaj göndermesini
     * sağlar.
     * 
     * @param senderClient
     * @param message 
     */
    public void broadcast(ServerClient senderClient, String message)
    {
        for(ServerClient client : clients.values())
        {
            // Mesajı gönderen istemci bu gönderimden hariç tutuluyor.
            if (!client.toString().equals(senderClient.toString()))
                client.sendLine(message);
        }
    }

    /**
     * Sunucu uygulamasını bağlatan ana metod.
     * 
     * @param args 
     */
    public static void main( String[] args )
    {
        Server.getInstance().listen(9090);
    }
}
