package Pathing;

import java.util.ArrayList;

import interfaces.Followable;
import interfaces.FollowableGridBased;
import tools.*;

public class BezeirCurve implements FollowableGridBased, Followable{
    private double ppc;
    private ArrayList <Line>curve = new ArrayList<Line>();
    private int i = 1;
    //x is the starting x cordinate
    //y is the starting y cordinate 
    //angle is the starting angle between -180 and 180
    //ppc is the number of points in each curve
    public BezeirCurve(double x,double y,double angle, double ppc){
        this.ppc = ppc;
        curve.add(new Line(x,y,angle));
    }

    public BezeirCurve(ArrayList<Line> targets,double ppc){
        curve.add(targets.remove(0));
        this.ppc = ppc;
        for (Line a : targets) {
            addTarget(a);
        }
        targets.add(0, curve.get(0));
    }

    public BezeirCurve(Line l, double ppc){
        this.ppc = ppc;
        curve.add(l);
    }
    public void setPPC(double ppc) {
        this.ppc = ppc;
    }
    
    public void addTarget(Line target){
        addTarget(target.getX(),target.getY(),target.getAngle());
    }
    //generates a new set of refence points for the path generator 
    public void addTarget(double x ,double y, double angle){
        Line last = curve.get(curve.size()-1);//grabs the last point in the current path as the first reference point
        Line target = new Line(x,y,angle);
        ArrayList <Line> temp = new ArrayList<Line>();
        temp.add(last);
        if (last.getAngle() != target.getAngle() && last.getSlope() == target.getSlope())
            System.out.println("I can't make a J turns");//this is a curve without enough information to make it smooth
        else if (last.getAngle() == target.getAngle()){
            double avgX = MathCont.avg(last.getX(),target.getX());
            double avgY = MathCont.avg(last.getY(),target.getY());
            double intAngle = MathCont.avg(last.getAngle(),target.getAngle())+90.;
            Line tempL = new Line(avgX, avgY, intAngle);
            temp.add(last.findInterctLine(tempL));
            temp.add(target.findInterctLine(tempL));
            temp.add(target);
            
        } else {
            temp.add(last.findInterctLine(target));
            temp.add(target);
        }
        for (Line d : temp) {
            System.out.println(d);
        }
        ArrayList<Line> portion = addCurve(temp);
        double finalAngle = portion.get(portion.size()-1).getAngle();
        if (!Comparitor.within(finalAngle,angle,90)){
            System.out.println("The path was still made however it might be impossible");
        }
        portion.get(portion.size()-1).setAngle(angle);
        portion.get(portion.size()-1).setX(x);
        portion.get(portion.size()-1).setY(y);
        curve.addAll(portion);
    }
    
    //returns the string describing all the points
    public String toString() {
        int count = 0;
        String temp = "";
        for (Line pos : curve) {
            temp += count + ": " + pos.toString() + "\n";
            count++;
        }
        return temp;
    }

    //This is how it calculates the points on  the path
    private ArrayList<Line> addCurve(ArrayList <Line> pos){
        int n = pos.size()-1;
        ArrayList<Line> portion = new ArrayList<Line>();
        portion.add(curve.get(curve.size()-1));
        for (double t = 1.0/ppc ; t<1+.5/ppc;t+=1.0/ppc){
            double pointX = 0;
            double pointY = 0;
            for(int i = 0;i <pos.size();i++){
                pointX += MathCont.choose(n,i)*Math.pow(1-t,n-i)*Math.pow(t,i)*pos.get(i).getX();
                pointY += MathCont.choose(n,i)*Math.pow(1-t,n-i)*Math.pow(t,i)*pos.get(i).getY();
            }
            double angle,deltaX, deltaY, deltaD, totalD;
            deltaX = pointX-portion.get(portion.size()-1).getX();
            deltaY = pointY-portion.get(portion.size()-1).getY();
            angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
            deltaD = Math.sqrt(deltaX*deltaX+deltaY*deltaY);
            totalD = curve.get(curve.size()-1).getDistance()+deltaD;
            portion.add(new Line(pointX,pointY,angle, totalD));
        }
        portion.remove(0);
        return portion;
    }

    //returns the last point in the curve
    public double getFinalAngle(){
        return curve.get(curve.size()-1).getAngle();
    }
    //returns the distance to the last point on the grid
    public double getDistanceToEnd(double x, double y) {
        return curve.get(curve.size()-1).distanceTo(x, y);
    }

    //uses the x and y cordiantes of the robot to find the target heading for the robot
    public double getTargetAngle(double x, double y){
        double lastDistance = curve.get(i-1).distanceTo(x, y);
        int c;
        for (c = i-1;c<curve.size()-1;c++){
            if (lastDistance < curve.get(c).distanceTo(x, y)){
                break;
            }else{
                lastDistance = curve.get(c).distanceTo(x, y);
                i = c;
            }
        }
        System.out.println(c);
        double deltaX = curve.get(c).getX()-x;
        double deltaY = curve.get(c).getY()-y;
        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        return angle;
    }
    public double getTargetDistance(double x,double y){
        double lastDistance = curve.get(i-1).distanceTo(x, y);
        int c;
        for (c = i-1;c<curve.size()-1;c++){
            if (lastDistance < curve.get(c).distanceTo(x, y)){
                break;
            }else{
                lastDistance = curve.get(c).distanceTo(x, y);
            }
        }
        return curve.get(c).distanceTo(x,y);
    }
    @Override
    public double getAngle(double distance) {
        for (int i = 1; i < curve.size()-1; i++){
            if(curve.get(i).getDistance() > distance){
                return curve.get(i-1).getAngle();
            }
        }
        return curve.get(curve.size()-1).getAngle();

    }
    @Override
    public double getReverseAngle(double distance) {
        double angle = getAngle(distance);
    	if (angle >= 0)
    		angle -= 180;
    	else
    		angle += 180;
    	return angle;
    }
    @Override
    public double getDistance(){
        return curve.get(curve.size()-1).getDistance();
    }
    @Override
    public double getFinalAngle(boolean foreward) {
        double angle = curve.get(curve.size()-1).getAngle();
        if (foreward)
            return angle;
    	if (angle >= 0)
    		angle -= 180;
    	else
    		angle += 180;
    	return angle;
    }
    @Override
    public double getTargetAngle(double x, double y, boolean foreward) {
        double angle =getTargetAngle(x, y);
        if (foreward)
            return angle;
    	if (angle >= 0)
    		angle -= 180;
    	else
    		angle += 180;
    	return angle;
    }

    public ArrayList<Line> getPath(){
        return curve;
    }

}
