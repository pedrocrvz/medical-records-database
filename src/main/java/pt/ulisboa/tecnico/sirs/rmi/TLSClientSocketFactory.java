package pt.ulisboa.tecnico.sirs.rmi;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

public class TLSClientSocketFactory implements RMIClientSocketFactory, Serializable {
    private final static String CIPHERSUITE = "TLS_RSA_WITH_AES_128_CBC_SHA256";

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket s = (SSLSocket) factory.createSocket(host, port);

        s.setUseClientMode(true);
        s.setEnabledProtocols(new String[]{"TLSv1.2"});
        s.setEnabledCipherSuites(new String[]{CIPHERSUITE});

        return s;
    }
}