package pt.ulisboa.tecnico.sirs.rmi;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

/**
 * Defines a client socket to connect to a secure server socket
 */
public class TLSClientSocketFactory implements RMIClientSocketFactory, Serializable {
    private final static String CIPHERSUITE = "TLS_RSA_WITH_AES_128_CBC_SHA256";


    /**
     * Method to connect to confidential and authenticated server sockets. This method established a socket to a server socket. This uses TLSv1.2, with cipher suite TLS_RSA_WITH_AES_128_CBC_SHA256. Demands client authentication.
     * The trust store is given as a java property, as well as the private key.
     * @param host Host
     * @param port Port
     * @return Returns a secure socket
     * @throws IOException Thrown when socket runs into problem
     */
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