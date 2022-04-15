package src.hashAlgorithms;

import java.security.NoSuchAlgorithmException;

public interface HashAlgorithmInterface {

    String hashFunc(String message) throws NoSuchAlgorithmException;
}
