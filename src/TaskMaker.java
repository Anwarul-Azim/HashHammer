package src;

import src.hashAlgorithms.HashAlgorithmInterface;
import src.hashAlgorithms.Sha256;
import src.passwordGen.PasswordInfo;
import src.passwordGen.PasswordMaker;
import src.passwordGen.PasswordTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class TaskMaker {

    private final String HASHED_PASS;
    private final PasswordTypes PASSWORD_TYPE;
    private final int PASSWORD_LENGTH;
    private final int THREAD_NO;

//    Set by makeTask()
    private List<Callable<String>> callableTasks;
    private long globalLowerLimit;
    private long globalUpperLimit;

//    Set by arrangeTask()
    private Function<Long, String> passGenerator;


    public TaskMaker(PasswordInfo passwordInfo) {
        this.HASHED_PASS = passwordInfo.hashedPass;
        this.PASSWORD_TYPE = passwordInfo.passwordType;
        this.PASSWORD_LENGTH = passwordInfo.length;
        this.THREAD_NO = passwordInfo.threadNo;
        makeTask(passwordInfo);
        arrangeTask();
    }

    public List<Callable<String>> getTasks() {
        return this.callableTasks;
    }

    private void makeTask(PasswordInfo passwordInfo) {
        switch (passwordInfo.passwordType) {
            case NUMERIC:
                globalLowerLimit = (long) Math.pow(10, PASSWORD_LENGTH - 1);
                globalUpperLimit = (long) Math.pow(10, PASSWORD_LENGTH);
                passGenerator = (Long l) -> String.valueOf(l);
                break;
            default:
                PasswordMaker passwordMaker = new PasswordMaker(PASSWORD_TYPE.getFloor(),
                        PASSWORD_TYPE.getCeil(),
                        passwordInfo.length);

                globalLowerLimit = passwordMaker.getMinPassNumericVal();
                globalUpperLimit = passwordMaker.getMaxPassNumericVal();
                passGenerator = passwordMaker.getPassGenerator();
        }
    }

    private void arrangeTask() {
        this.callableTasks = new ArrayList<>();
        long subSpaceSize = globalUpperLimit / THREAD_NO;
        for (long i = 0; i < THREAD_NO; i++) {
            long finalI = i;
            callableTasks.add(() -> {
                long subSpaceMin = globalLowerLimit + (finalI * subSpaceSize);
                long subSpaceMax = subSpaceMin + subSpaceSize;
                long counter = subSpaceMin;

                String password = passGenerator.apply(counter);
                HashAlgorithmInterface sha256 = new Sha256();
                while (!sha256.hashFunc(password).equals(HASHED_PASS)) {
                    if (counter <= subSpaceMax) {
                        counter++;
                    } else {
                        throw new InterruptedException();
                    }
                    password = passGenerator.apply(counter);
                }
                return password;
            });
        }
    }

}
