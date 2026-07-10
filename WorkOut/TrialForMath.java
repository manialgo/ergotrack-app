package com.ergotrack;

public class TrialForMath{
	
	// static for this class reference for usage for the univeral make it to public
	static class Point{
		double x;
		double y;
		Point(double x, double y){
			this.x = x;
			this.y = y;
		}
	}
	
	public static void main(String[] args) {
		Point A = new Point(5.0, 5.0);
		Point B = new Point(0,0);
		
		double angle = Math.atan2(A.x-B.x, A.y-B.y);
		System.out.println(angle);
		//0.7853981633974483
		
		double angle2 = Math.atan2(5.0, 5.0);
		System.out.println(angle2);
		//0.7853981633974483

		/* Observation: It calculates the angle from the base x axis that is the y=0 line in cartesian system
		 * So it returns the same radian value
		 * it measures from the origin point
		 *
		 *
		 *
         * atan2 method will return the angle that raised between two points
         * without the proper abscissa and ordinate it takes from the origin
         * so we are fixing that hip point to origin and make their subtraction for calculation accuracy
         * then repeat for the knee point too 
         * */
		
	}
}
