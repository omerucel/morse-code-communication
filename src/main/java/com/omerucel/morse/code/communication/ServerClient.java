/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omerucel.morse.code.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

/**
 * Sunucuya bağlanan her istemci için bu sınıftan bir örnek oluşturulur. Bu
 * örnekler yeni bir thread altında çalışır.
 * 
 * @author omer
 */
public class ServerClient implements Runnable{
    // Sunucuya bağlanan istemci için açılan socket.
    Socket socket;

    // İstemciden gelen bilgileri toplamak için kullanılan obje.
    BufferedReader in;

    // İstemciye bilgi göndermek için kullanılan obje.
    PrintWriter out;

    // İstemci için üretilen özel numara.
    String uuid = null;

    /**
     * Bağlatıcı method. Bu metod içerisinde istemci için özel bir numara
     * üretilir. Bu numara, Server objesindeki clients isimli key-value tabanlı
     * listede, bazı durumlarda istemciye erişmek için kullanılır.
     * 
     * @param socket 
     */
    public ServerClient(Socket socket)
    {
        this.socket = socket;
        this.uuid = UUID.randomUUID().toString();
    }

    /**
     * toString metodunu ezerek, istemci için üretilen numaranın dönülmesi sağlandı.
     * 
     * @return 
     */
    @Override
    public String toString()
    {
        return uuid;
    }

    /**
     * Thread çalıştığında bu metod devreye girer.
     */
    public void run()
    {
        try {
            // İstemciden gelen ve istemciye gönderilen bilgileri toplamak ve yazmak
            // için ilgili objeler üretiliyor.
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Yeni bir istemci bağlantı haberi syncronized olan metod ile
            // sunucuya bildiriliyor.
            Server.getInstance().clientConnected(this);

            // Socket açık kaldığı müddetçe istemciden bilgiler alınıyor.
            String line;
            while(socket.isConnected())
            {
                line = in.readLine().trim();
                System.out.println("Received : " + line);

                if (line == null) continue;

                // İstemciden alınan bilgi, tüm bağlı istemcilere gönderiliyor.
                Server.getInstance().broadcast(this, line);
            }

            // Sunucu bağlantısı kopan istemci için syncronized olan metod çalıştırılıyor
            // ve ilgili veri yapılarından siliniyor.
            Server.getInstance().clientDisconnected(this);
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    /**
     * İstemciye mesaj gönderebilmek için yardımcı metod.
     * 
     * @param message 
     */
    public void sendLine(String message)
    {
        out.println(message);
    }
}
