package pt.ulisboa.tecnico.sirs.security;

import java.io.IOException;

/**
 * This interface defines the means of a class being Signed by another entity
 */
public interface Signable {
    /**
     * This method returns the object bytes to be signed by an entity
     * @return byte array corresponding to class object
     */
    byte[] getBytes();
}
