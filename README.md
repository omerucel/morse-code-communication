Paket Oluşturma
===============

Sunucu ve İstemci uygulamalarının paketleri aşağıdaki komutlar çalıştırıldığında
target dizininde oluşturulur. Komutlar için sistemde maven kurulu olmalıdır.

mvn package -P Server
mvn package -P Client

Uygulamanın Çalıştırılması
==========================

Paketler oluştuğunda, target dizininde Server-jar-with-dependencies.jar ve
Client-jar-with-dependencies.jar dosyaları oluşur.

Sunucuyu çalıştırmak için aşağıdaki komut kullanılır. Bu komut ile 9090 portunu
dinleyen sunucu çalışmaya başlar:

java -jar target/Server-jar-with-dependencies.jar

İstemci uygulamasını çalıştırmak için aşağıdaki komut kullanılır:

java -jar target/Client-jar-with-dependencies.jar

Uygulama İçinde Kullanılan Kütüphaneler
=======================================

text-to-speech için FreeTTS kütüphanesinden faydalanılmıştır.

Morse kodu dönüştürme işlemi için net.jtank.protocol.MorseCode sınıfından
yararlanılmıştır. (http://tanksoftware.com/jtank/src/showsrc.php?src=src/net/jtank/protocol/MorseCode.java)