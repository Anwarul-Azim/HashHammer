package src;

import src.hashAlgorithms.HashAlgorithmInterface;
import src.hashAlgorithms.Sha256;
import src.passwordGen.PasswordInfo;
import src.passwordGen.PasswordMaker;
import src.passwordGen.PasswordTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class TaskMaker {
    private final String hashedPass;
    private final PasswordTypes passwordType;
    private final int passwordLength;
    private final int threadNo;
    private List<Callable<String>> callableTasks;


    long lowerLimit;
    long upperLimit;
    Function<Long, String> makePass;


    public TaskMaker(PasswordInfo passwordInfo) {
        this.hashedPass = passwordInfo.hashedPass;
        this.passwordType = passwordInfo.passwordType;
        this.passwordLength = passwordInfo.length;
        this.threadNo = passwordInfo.threadNo;
        makeTask(passwordInfo);
        arrangeTask();
    }


    void makeTask(PasswordInfo passwordInfo) {
        switch (passwordInfo.passwordType) {
            case NUMERIC:
                lowerLimit = (long) Math.pow(10, passwordLength - 1);
                upperLimit = (long) Math.pow(10, passwordLength);
                makePass = (Long l) -> String.valueOf(l);
                break;
            default:
                PasswordMaker passwordMaker = new PasswordMaker(passwordType.getFloor(),
                        passwordType.getCeil(),
                        passwordInfo.length);

                lowerLimit = passwordMaker.getMinPassVal();
                upperLimit = passwordMaker.getMaxPassVal();
                makePass = passwordMaker.makePassword;
        }
        this.callableTasks = new ArrayList<>();

    }

    void arrangeTask() {
        long distance = upperLimit / threadNo;
        for (long i = 0; i < threadNo; i++) {
            long finalI = i;
            callableTasks.add(() -> {
                long min_ = lowerLimit + (finalI * distance);
                long max_ = min_ + distance;
                long count = min_;
                boolean flag = true;
                //JUST AN ASSIGNMENT
                String password = makePass.apply(lowerLimit);
                HashAlgorithmInterface sha256 = new Sha256();
                while (!sha256.hashFunc(password).equals(hashedPass)) {
                    if (count < max_) {
                        count++;
                    } else if (flag) {
                        flag = false;

                    }
                    password = makePass.apply(count);
                }
                return password;
            });
        }
    }
    List<Callable<String>> getTasks() {
        return this.callableTasks;
    }

}
