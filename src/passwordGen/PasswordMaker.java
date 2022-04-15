package src.passwordGen;

import java.util.function.Function;

public class PasswordMaker {
    private int charSpaceSize;
    private int startingVal;
    private long minPassVal;
    private long maxPassVal;

    public PasswordMaker(char floor, char ceil, int length) {
        this.charSpaceSize = ceil - floor + 1;
        this.startingVal = floor - 1;
        this.minPassVal = symbolToNumber((floor + "").repeat(length));
        this.maxPassVal = symbolToNumber((ceil + "").repeat(length));
    }

    public Function<Long, String> makePassword = (Long n) -> {
        long number = n;
        String answer = "";

        while (number > 0) {
            long pos = (number % charSpaceSize);
            if (pos == 0) {
                pos = charSpaceSize;
                number--;
            }
            answer = (char) (pos + startingVal - 1) + answer;
            number = number / charSpaceSize;
        }
        return answer;
    };

    private long symbolToNumber(String columnTitle) {
        double output = 0;
        long pos = 0;
        for (int i = columnTitle.length() - 1; i >= 0; i--) {
            output = output + ((long)columnTitle.charAt(i) - startingVal) * Math.pow(charSpaceSize,pos);
            pos++;
        }
        return (long)output;
    }

    public long getMaxPassVal() {
        return maxPassVal;
    }
    public long getMinPassVal() {
        return minPassVal;
    }
}
