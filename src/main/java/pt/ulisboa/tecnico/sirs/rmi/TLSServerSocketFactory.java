package pt.ulisboa.tecnico.sirs.rmi;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;

/**
 * Defines a server socket that accepts connections from client socket
 */
public class TLSServerSocketFactory implements RMIServerSocketFactory, Serializable {
    private final static String CIPHERSUITE = "TLS_RSA_WITH_AES_128_CBC_SHA256";

    /**
     * Method to create confidential and authenticated server sockets. This method binds a socket with given port. This uses TLSv1.2, with cipher suite TLS_RSA_WITH_AES_128_CBC_SHA256. Demands client authentication.
     * The trust store is given as a java property, as well as the private key.
     * @param port Port to bind the socket
     * @return Returns a secure socket
     * @throws IOException Thrown when socket runs into problem
     */
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