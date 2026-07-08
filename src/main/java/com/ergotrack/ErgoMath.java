package com.ergotrack;

public class ErgoMath {

    public static class Point {
        double x;
        double y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static double calculateAngle(Point A, Point B, Point C) {
        
        double angleBA = Math.atan2(A.y - B.y, A.x - B.x);
        double angleBC = Math.atan2(C.y - B.y, C.x - B.x);
        
        double radians = angleBA - angleBC;
        
        double degrees = Math.abs(radians * 180.0 / Math.PI);
        
        if (degrees > 180.0) {
            degrees = 360.0 - degrees;
        }
        
        return degrees;
    }
    
}
