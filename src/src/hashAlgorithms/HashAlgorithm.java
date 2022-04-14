package src.hashAlgorithms;

import java.security.NoSuchAlgorithmException;

public interface HashAlgorithm {

    String hashFunc(String message) throws NoSuchAlgorithmException;
}
