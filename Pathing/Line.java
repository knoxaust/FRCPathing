package Pathing;

public class Line {
    private double x, y, angle,slope,distance;

    public Line(double x, double y, double angle, double distance){
        this.x = x;
        this.y = y;
        this.angle = angle;
        double rad = Math.toRadians(angle);
        if ((angle + 90)%180 != 0)
        slope = Math.tan(rad);
        else{
            slope = Double.MAX_VALUE;
        }
        this.distance = distance;
    }
    public Line(double x, double y, double angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
        double rad = Math.toRadians(angle);
        if ((angle + 90)%180 != 0)
        slope = Math.tan(rad);
        else{
            slope = Double.MAX_VALUE;
        }
        distance = 0;
    }
    public Line(double x, double y){
        this.x = x;
        this.y = y;
        angle = 0;
        slope = 0;
    }

    public double getX(){
        return x;
    }
    public double getY() {
        return y;
    }
    public double getAngle(){
        return angle;
    }
    public double getSlope(){
        return slope;
    }
    public double getDistance(){
        return distance;
    }
    public void setX(double x){
        this.x =x;
    }
    public void setY(double y){
        this.y = y;
    }
    public void setDistance(double distance){
        this.distance = distance;
    }
    public void setAngle(double angle){
        this.angle = angle;
        double rad = Math.toRadians(angle);
        if ((angle + 90)%180 != 0)
        slope = Math.tan(rad);
        else{
            slope = Double.MAX_VALUE;
        }
    }
    public int pointingQuad(){
        if (angle <-90)
        return 3;
        if (angle <0)
        return 4;
        if (angle <90)
        return 1;
        return 2;
    }
    public int inverseQuad(){
        int temp = pointingQuad();
        temp-=2;
        if (temp <0)
            temp+=4;
        return temp;
    }
    public int quadrentRelation(Line temp){
        double tempX = temp.getX()-x;
        double tempY = temp.getY()-y;
        if (tempX>0 && tempY >0)
        return 1;
        if (tempX<0 && tempY>0)
        return 2;
        if (tempX<0 && tempY<0)
        return 3;
        return 4;
    }
    public double distanceTo(double x, double y){
        double deltaX, deltaY;
        deltaX = this.x-x;
        deltaY = this.y-y;
        return Math.sqrt(deltaX*deltaX+deltaY*deltaY);
    }
    public Line findInterctLine(Line a){
        double tempX, tempY;
        if (slope == Double.MAX_VALUE){
            tempX = x;
        }else if (a.getSlope() == Double.MAX_VALUE){
            tempX = a.getX();
        }else{
            double part1 = -1*a.getSlope()*a.getX();
            double part2 = x*slope;
            double part3 = -1*y;
            double numerator = part1+part2+part3+a.getY();
            double denominator = slope-a.getSlope();
        if(denominator == 0)
            tempX = x;
        else
            tempX = numerator/denominator;
        }
        if((angle+90)%180 != 0)
            tempY = slope * (tempX - x) + y;
        else
            tempY = a.getSlope() * (tempX - a.getX()) + a.getY();
        return new Line (tempX,tempY);
    }
    public String toString(){
        return " x " + x+ " y " + y + " angle " + angle + " slope " + slope + " distance " + distance;
    }

}
