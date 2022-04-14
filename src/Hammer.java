package src;

import src.hashAlgorithms.HashAlgorithm;
import src.hashAlgorithms.Sha256;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class Hammer {

    private final String HASHED_PASS;
    private final int LENGTH;
    private final PasswordType PASSWORD_TYPE;
    private final int THREAD_NO;

    public Hammer(String hashedPass, int length, PasswordType passwordType, int threadNo) {
        this.HASHED_PASS = hashedPass;
        this.LENGTH = length;
        this.PASSWORD_TYPE = passwordType;
        this.THREAD_NO = threadNo;
    }

    public void start() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        AtomicReference<LocalDateTime> now = new AtomicReference<>(LocalDateTime.now());
        System.out.println(dtf.format(now.get()));

        long lowerLimit = (long) Math.pow(10, LENGTH);
        long upperLimit = (long) Math.pow(10, LENGTH - 1);
        long DISTANCE = upperLimit / THREAD_NO;

        ExecutorService exctr = Executors.newFixedThreadPool(THREAD_NO);
        List<Callable<String>> cList = new ArrayList<>();

        for (long i = 0; i < THREAD_NO; i++) {
            long finalI = i;
            cList.add(() -> {
                long min_ = lowerLimit + (finalI * DISTANCE);
                long max_ = min_ + DISTANCE;
                long count = min_;
                boolean flag = true;
                //JUST AN ASSIGNMENT
                String message = String.valueOf(lowerLimit);
                HashAlgorithm sha256 = new Sha256();
                while (!sha256.hashFunc(message).equals(HASHED_PASS)) {
                    if (count < max_) {
                        count++;
                    } else if (flag) {
                        flag = false;
                        now.set(LocalDateTime.now());
                        System.out.println(dtf.format(now.get()));
                    }
                    message = String.valueOf(count);
                }
                return message;
            });
        }

        String ans = "";
        try {
            ans = exctr.invokeAny(cList);
            exctr.shutdownNow();
        } catch (InterruptedException | ExecutionException e) {
            exctr.shutdownNow();
            e.printStackTrace();
        }
        System.out.println("\n"+ ans);
        now.set(LocalDateTime.now());
        System.out.println(dtf.format(now.get()));
    }
}

