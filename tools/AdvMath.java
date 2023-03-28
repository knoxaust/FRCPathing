package tools;


/*
This is a continuation of the matt class to add factorials and choose functions
*/
public class MathCont {
    
    public static int factorial(int num){
        if (num <= 1)
            return 1;
        return num * factorial(num-1);
    }
    public static double choose(int n, int i){
        return factorial(n)/(double)(factorial(i)*factorial(n-i));
    }
    public static double avg(double a, double b){
        return (a+b)/2.;
    }

    public static double avg (double[] a){
        double sum = 0;
        for (Double object : a) {
            sum +=object;
        }
        return sum/a.length;
    }
    public static double rangeAngleDeg(double val){
        val %= 360;
        if (val > 180){
            val = -360+val;
        }else if (val < -180){
            val = 360+val;
        }
        return val;
    }
}
