package com.omerucel.morse.code.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import net.jtank.protocol.MorseCode;

/**
 * Grafiksel uygulama üzerinden bu sınıf vasıtasıyla sunucuyla iletişim kurulur.
 * 
 * @author omer
 */
public class Client implements Runnable{
    // Sunucu ile iletişimi sağlayan obje.
    Socket socket;

    // Sunucudan gelen mesajları toplamak için kullanılan obje.
    BufferedReader in;

    // Sunucuya mesaj göndermek için kullanılan obje.
    PrintWriter out;

    /**
     * Gönderilen host ve port bilgileri ile bu sınıfın yeni bir örneğini 
     * oluşturur.
     * 
     * @param host
     * @param port
     * @return
     * @throws IOException 
     */
    public static Client newInstance(String host, int port) throws IOException
    {
        Socket socket = new Socket(host, port);
        return new Client(socket);
    }

    /**
     * Başlatıcı method.
     * 
     * @param socket 
     */
    public Client(Socket socket)
    {
        this.socket = socket;
    }

    /**
     * Thread çalıştığında bu method devreye girer.
     */
    public void run()
    {
        // Sunucuya bağlanıldığı için arabirimdeki ilgili buton ve inputbox lar
        // aktif hale getirirlir.
        WindowClient.getInstance().loading(false);

        try {
            // Sunucuya bağlı olan socket üzerinden gelen bilgiler için
            // BufferedReader örneği oluşturulur.
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Sunucuya gönderilecek bilgileri için PrintWriter örneği oluşturulur.
            out = new PrintWriter(socket.getOutputStream(), true);

            // Sunucudan gelen yanıtlar alınıyor.
            String line;
            while((line = in.readLine().trim()) != null)
            {
                // Gelen morse kodlu yanıt dönüştürülüyor ve arabirim üzerinde
                // gösterilmek üzere WindowClient objesi yardımcı metoduna aktarılıyor.
                WindowClient.getInstance().receivedLine(MorseCode.stringFromMorse(line));
            }

        } catch (IOException ex) {
            WindowClient.getInstance().loading(false);
            System.out.println(ex.getStackTrace());
        }
    }

    /**
     * Sunucuya veri gönderimi için kullanılıyor. Gönderilmek istenen mesajı
     * MorseCode objesi ile morse koduna çevirip sunucuya yollar.
     * 
     * @param message 
     */
    public void sendLine(String message)
    {
        out.println(MorseCode.stringToMorse(message));
    }
}
