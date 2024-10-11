import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class SSLServer {

    public static void main(String[] args) throws Exception {
        // Carregar o KeyStore
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream keyStoreStream = new FileInputStream("keystore.jks")) {
            keyStore.load(keyStoreStream, "password".toCharArray());
        }

        // Criar KeyManagerFactory e SSLContext
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, "password".toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        // Criar SSLServerSocket
        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
        try (SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(8443)) {
            System.out.println("Servidor SSL/TLS iniciado...");
            
            while (true) {
                try (SSLSocket socket = (SSLSocket) serverSocket.accept()) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write("Conexão segura estabelecida!");
                    writer.flush();
                }
            }
        }
    }
}
