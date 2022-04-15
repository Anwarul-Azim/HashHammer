package src.passwordGen;

import src.hashAlgorithms.HashAlgorithms;

public class PasswordInfo {
    public final String hashedPass;
    public final int length;
    public final PasswordTypes passwordType;
    public final HashAlgorithms hashAlgorithm;
    public final int threadNo;



    public PasswordInfo(
            String hashedPass, int length, PasswordTypes passwordType,
            HashAlgorithms hashAlgorithm,  int threadNo) {
        this.hashedPass = hashedPass;
        this.length = length;
        this.passwordType = passwordType;
        this.hashAlgorithm = hashAlgorithm;
        this.threadNo = threadNo;

    }

}
