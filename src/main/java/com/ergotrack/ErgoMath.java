package com.ergotrack;

public class ErgoMath {

    // A simple inner class to hold coordinates (like a LeetCode TreeNode structure)
    public static class Point {
        double x;
        double y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Calculates the angle ABC at vertex B (the Hip)
     */
    public static double calculateAngle(Point A, Point B, Point C) {
        // Calculate angles of vectors BA and BC relative to the X-axis
        double angleBA = Math.atan2(A.y - B.y, A.x - B.x);
        double angleBC = Math.atan2(C.y - B.y, C.x - B.x);
        
        // Find the absolute difference in radians
        double radians = angleBA - angleBC;
        
        // Convert radians to degrees
        double degrees = Math.abs(radians * 180.0 / Math.PI);
        
        // We want the interior angle (always <= 180 degrees)
        if (degrees > 180.0) {
            degrees = 360.0 - degrees;
        }
        
        return degrees;
    }
    
}