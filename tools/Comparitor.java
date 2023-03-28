package tools;

public class Comparitor{
    
    //percent is as a decimal
    public static boolean withinPercent(double value, double comparitor, double percent) {
        return within(value , comparitor, percent*comparitor);
    }
    
    public static boolean within(double value, double comparitor, double range) {
        return value < comparitor + range && value > comparitor - range;
    }

    public static boolean between(double value, double lower, double upper){
        return value < upper && value > lower;
    }
}