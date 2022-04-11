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
        
        // sha256 digest for the value 1234567890 10 digit number
        String hash = "c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646";
        // For a n digit decimal number, MAX = 10^n, MIN = 10^(n - 1)
        long MAX = (long)Math.pow(10, 10);
        long MIN = (long)Math.pow(10, 9);
        int THREAD_NO = 100;
        long DISTANCE = MAX/THREAD_NO;         
        
        ExecutorService exctr = Executors.newFixedThreadPool(THREAD_NO);
        //List of callable tasks
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
        System.out.println("                          "+ ans);
        now.set(LocalDateTime.now());
        System.out.println(dtf.format(now.get()));
    }
}
