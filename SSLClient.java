import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class SSLClient {

    public static void main(String[] args) throws Exception {
        // Carregar o TrustStore
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (FileInputStream trustStoreStream = new FileInputStream("truststore.jks")) {
            trustStore.load(trustStoreStream, "password".toCharArray());
        }

        // Criar TrustManagerFactory e SSLContext
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        // Criar SSLSocket
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        try (SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("localhost", 8443)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            String response = reader.readLine();
            System.out.println("Mensagem do servidor: " + response);
        }
    }
}
