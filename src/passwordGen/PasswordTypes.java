package src.passwordGen;

public enum PasswordTypes {

    NUMERIC('0', '9'),
    LOWER_CASE_ONLY('a', 'z'),
    UPPER_CASE_ONLY('A', 'Z'),
    ALPHABETIC_MIXED_CASE('A', 'z'), // not optimized
    ALPHANUMERIC('0', 'z'), // not optimized
    ALPHANUMERIC_WITH_SPECIAL_CHARS(' ', '~'); // works

    final char floor;
    final char ceil;
    PasswordTypes(char floor, char ceil) {
        this.floor = floor;
        this.ceil = ceil;
    }

    public char getFloor(){
        return this.floor;
    }
    public char getCeil(){
        return this.ceil;
    }


}
