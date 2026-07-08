package com.ergotrack;

import com.ergotrack.ErgoMath.Point;

public class PositionOfObject {

    public static void main(String[] args) {
        
        System.out.println("--- Starting Ergonomic Math Dry Run ---");

        // Test Case 1: Standing Perfectly Straight (Should be 180 degrees)
        Point shoulder1 = new Point(5.0, 10.0);
        Point hip1      = new Point(5.0, 5.0);
        Point knee1     = new Point(5.0, 0.0);
        
        double straightAngle = ErgoMath.calculateAngle(shoulder1, hip1, knee1);
        System.out.println("Standing Straight Angle: " + straightAngle + "°"); 
        
        System.out.println("---------------------------------------");

        // Test Case 2: Bending Forward Halfway (Should be roughly 90 degrees)
        Point shoulder2 = new Point(10.0, 5.0); // Leaning out to the right
        Point hip2      = new Point(5.0, 5.0);  // Hip stays in place
        Point knee2     = new Point(5.0, 0.0);  // Legs stay vertical
        
        double bentAngle = ErgoMath.calculateAngle(shoulder2, hip2, knee2);
        System.out.println("Bending Forward Angle: " + bentAngle + "°");
        
        System.out.println("---------------------------------------");
    }
}
