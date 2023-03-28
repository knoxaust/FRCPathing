package interfaces;

public interface FollowableGridBased {
    public double getTargetAngle(double x, double y, boolean foreward);
    public double getDistanceToEnd(double x, double y);
    public double getFinalAngle(boolean foreward);
}
