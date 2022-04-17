package src.passwordGen;

import java.util.function.Function;

public class PasswordMaker {
    private final int CHAR_SPACE_SIZE;
    private final int STARTING_CHAR_VAL;
    private final long MIN_PASS_NUMERIC_VAL;
    private final long MAX_PASS_NUMERIC_VAL;
    private final Function<Long, String> PASS_GENERATOR;

    public PasswordMaker(char floor, char ceil, int length) {
        this.CHAR_SPACE_SIZE = ceil - floor + 1;
        this.STARTING_CHAR_VAL = floor - 1;
        this.MIN_PASS_NUMERIC_VAL = passToNumericVal((floor + "").repeat(length));
        this.MAX_PASS_NUMERIC_VAL = passToNumericVal((ceil + "").repeat(length));
        this.PASS_GENERATOR = (Long n) -> {
            long number = n;
            String answer = "";

            while (number > 0) {
                long pos = (number % CHAR_SPACE_SIZE);
                if (pos == 0) {
                    pos = CHAR_SPACE_SIZE;
                    number--;
                }
                answer = (char) (pos + STARTING_CHAR_VAL - 1) + answer;
                number = number / CHAR_SPACE_SIZE;
            }
            return answer;
        };
    }

    public long getMaxPassNumericVal() {
        return MAX_PASS_NUMERIC_VAL;
    }

    public long getMinPassNumericVal() {
        return MIN_PASS_NUMERIC_VAL;
    }

    public Function<Long, String> getPassGenerator() {
        return this.PASS_GENERATOR;
    }


    private long passToNumericVal(String password) {
        double val = 0;
        long charPosition = 0;
        for (int i = password.length() - 1; i >= 0; i--) {
            val = val + ((long) password.charAt(i) - STARTING_CHAR_VAL) * Math.pow(CHAR_SPACE_SIZE, charPosition);
            charPosition++;
        }
        return (long) val;
    }

}
