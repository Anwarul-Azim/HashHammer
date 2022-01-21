package com.company;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

//876f495e8da8e0958b619778ee8cc3914fe74cd41c80fad11176c4cc05a4e171
public class Main {
    static int complete = 1;
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }
    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        AtomicReference<LocalDateTime> now = new AtomicReference<>(LocalDateTime.now());
        System.out.println(dtf.format(now.get()));
        long MAX = 100000000000L;
        long MIN = 10000000000L;
        int THREAD_NO = 100;
        long DISTANCE = MAX/THREAD_NO;


        //836438d03e80c2ba6d387089bd6872c9cc75a450cf64b15f40d8a62018db2742
        //String hash = "876f495e8da8e0958b619778ee8cc3914fe74cd41c80fad11176c4cc05a4e171";
        String hash = "876f495e8da8e0958b619778ee8cc3914fe74cd41c80fad11176c4cc05a4e171";//test
        ExecutorService exctr = Executors.newFixedThreadPool(THREAD_NO);
        //callable tasks
        List<Callable<String>> cList = new ArrayList<>();
        //populating the callable task list

        for (long i = 0; i < THREAD_NO; i++) {
            long finalI = i;
            cList.add(() -> {
                long min_ = MIN + (finalI * DISTANCE) ;
                long max_ = min_ + DISTANCE;
                long count = min_;
                boolean flag = true;
                //JUST AN ASSIGNMENT
                String ans = String.valueOf(MIN);
                while(!toHexString(getSHA(ans)).equals(hash)) {
                    if(count < max_) {
                        count++;
                    } else if(flag) {
                        flag = false;
                        System.out.println(complete);
                        complete = complete + 1;
                        now.set(LocalDateTime.now());
                        System.out.println(dtf.format(now.get()));
                    }
                    ans = String.valueOf(count);
                }
                return ans;
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
        System.out.println(ans + "\n                                    Bingo");
        now.set(LocalDateTime.now());
        System.out.println(dtf.format(now.get()));


    }
}