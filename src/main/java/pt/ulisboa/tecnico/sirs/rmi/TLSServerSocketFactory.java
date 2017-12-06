package pt.ulisboa.tecnico.sirs.rmi;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;

public class TLSServerSocketFactory implements RMIServerSocketFactory, Serializable {
    private final static String CIPHERSUITE = "TLS_RSA_WITH_AES_128_CBC_SHA256";

    public ServerSocket createServerSocket(int port) throws IOException {
        SSLServerSocketFactory sf = ((SSLServerSocketFactory) SSLServerSocketFactory.getDefault());
        SSLServerSocket s = (SSLServerSocket) (sf.createServerSocket(port));

        s.setUseClientMode(false);
        s.setEnabledProtocols(new String[]{"TLSv1.2"});
        s.setEnabledCipherSuites(new String[]{CIPHERSUITE});
        s.setNeedClientAuth(true);
        s.setEnableSessionCreation(true);

        return s;
    }
}