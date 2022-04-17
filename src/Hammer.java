package src;


import src.hashAlgorithms.HashAlgorithms;
import src.passwordGen.PasswordInfo;
import src.passwordGen.PasswordTypes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class Hammer {
    static String margin;
    static DateTimeFormatter dtf;
    static AtomicReference<LocalDateTime> now;

    static {
        dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        now = new AtomicReference<>(LocalDateTime.now());
        margin = "\n----------------------\n";
    }


    public static void main(String[] args) {

        //*****************************************************

        String hash = "3c655b2e44f33c8144a23a9fee19ebcp89e1350264ff73965659e606613b2d3e";
        int length = 6;
        PasswordTypes type = PasswordTypes.NUMERIC;
        HashAlgorithms algo = HashAlgorithms.SHA_256;
        int threadNo = 100;

        //*****************************************************

        PasswordInfo input = new PasswordInfo(hash, length, type, algo, threadNo);
        TaskMaker taskMaker = new TaskMaker(input);
        ExecutorService executor = Executors.newFixedThreadPool(threadNo);

        System.out.print(margin + "Hash: " + hash + "\nPassword Length: "+ length +"\n" +dtf.format(now.get()) + margin);
        String ans = "";
        try {
            ans = executor.invokeAny(taskMaker.getTasks());
            System.out.println("Password : " +  ans);
            executor.shutdownNow();
        } catch (InterruptedException | ExecutionException e) {
            executor.shutdownNow();
            System.out.println("No message found from the given hash within the range.\nCheck if the \"password length\"/ \"hash string\" is correct.");
        }
        now.set(LocalDateTime.now());
        System.out.print(dtf.format(now.get()) + margin);
    }
}

