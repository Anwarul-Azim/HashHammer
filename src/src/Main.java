package src;

public class Main {

    public static void main(String[] args) {
        String hash = "";
        int length = 8;
        PasswordType type = PasswordType.NUMERIC;
        int threadNo = 100;
        new Hammer(hash, length, type, threadNo).start();
    }

}