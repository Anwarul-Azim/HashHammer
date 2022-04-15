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

        String hash = "72f3f0ec0aecf5aed1277c3d0b5ad4de4fb631fd7ed216cd1db5b1920cc48342";
        int length = 6;
        PasswordTypes type = PasswordTypes.UPPER_CASE_ONLY;
        HashAlgorithms algo = HashAlgorithms.SHA_256;
        int threadNo = 100;

        //*****************************************************

        PasswordInfo input = new PasswordInfo(hash, length, type, algo, threadNo);
        TaskMaker taskMaker = new TaskMaker(input);
        ExecutorService executor = Executors.newFixedThreadPool(threadNo);

        System.out.print(margin + dtf.format(now.get()) + margin);
        String ans = "";
        try {
            ans = executor.invokeAny(taskMaker.getTasks());
            executor.shutdownNow();
        } catch (InterruptedException | ExecutionException e) {
            executor.shutdownNow();
            e.printStackTrace();
        }
        System.out.println(margin + ans);
        now.set(LocalDateTime.now());
        System.out.print(dtf.format(now.get()) + margin);
    }
}

