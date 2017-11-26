package pt.ulisboa.tecnico.sirs.exception;

import java.security.GeneralSecurityException; /**
 * This exception is thrown whenever Security library runs into problem
 */
public class SecurityLibraryException extends Exception {
    public SecurityLibraryException(Exception e) {
        super(e);
    }
}