package com.ergotrack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import com.ergotrack.ErgoMath.Point;

public class AngleMeasure {
    public static void main(String[] args) {
        // Initialize the webcam grabber (0 = default integrated laptop camera)
    		//	If more than a camera or to access for the secondary camera put 1,2,3 according to the
    		// port configuration in our laptop
    	
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        
        // A converter utility class to change native video frames into editable Java Images
        Java2DFrameConverter converter = new Java2DFrameConverter();
        
        try {
            grabber.start(); // Boot up camera hardware
            
            // Open a window display frame titled "Ergo-Track AI Safety Monitor"
            CanvasFrame canvas = new CanvasFrame("Ergo-Track AI Safety Monitor");
            canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            
            System.out.println("AI Engine Started. Monitoring posture angles...");

            // The Live Processing Loop (Running continuously frame-by-frame)
            while (canvas.isVisible()) {
            	
                Frame rawFrame = grabber.grab();
                
                if (rawFrame == null) continue;
                
                // Convert the raw native frame into a standard Java BufferedImage so we can draw on it
                BufferedImage image = converter.convert(rawFrame);
                if (image == null) continue;
                
                // Get the Graphics pen tool to draw overlays onto our video frame
                Graphics2D g2d = image.createGraphics();
                
                int centerX = image.getWidth() / 2;
                int centerY = image.getHeight() / 2;
                
                // Let's establish our joint coordinates using your custom inner Point class
                Point shoulder = new Point(centerX, centerY -80);
                Point hip      = new Point(centerX, centerY);
                Point knee     = new Point(centerX, centerY +100);
                
                // EXECUTE MATH DETECTOR: Pass tracked points directly to your geometric engine
                double currentHipAngle = ErgoMath.calculateAngle(shoulder, hip, knee);
                
                // ERGONOMIC RISK ASSESSMENT LOGIC
                // If the user's spine drops below 140 degrees, they are slouching/bending dangerously.
                boolean isRiskDetected = (currentHipAngle < 140.0);
                
                // REAL-TIME DIGITAL OVERLAY RENDERING
                // Draw the skeletal tracking lines joining our tracked nodes
                
                // Make the line thick
                g2d.setStroke(new java.awt.BasicStroke(4f));
                
                // Turn red if dangerous!
                g2d.setColor(isRiskDetected ? Color.RED : Color.GREEN);
                
                // Spine Line
                g2d.drawLine((int)shoulder.x, (int)shoulder.y, (int)hip.x,  (int)hip.y);
                // Thigh Line
                g2d.drawLine((int)hip.x, (int)hip.y, (int)knee.x, (int)knee.y);
                
                // Draw circular indicator tracking nodes over the joint locations
                g2d.setColor(Color.YELLOW);	
                g2d.fillOval((int)shoulder.x - 6, (int)shoulder.y - 6, 12, 12);
                g2d.fillOval((int)hip.x - 6, (int)hip.y - 6, 12, 12);
                g2d.fillOval((int)knee.x - 6, (int)knee.y - 6, 12, 12);
                
                // TEXT HEADS-UP DISPLAY (HUD)
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                g2d.setColor(Color.WHITE);
                g2d.drawString("Live Hip Angle: " + String.format("%.1f", currentHipAngle) + "°", 20, 40);
                
                if (isRiskDetected) {
                    g2d.setColor(Color.RED);
                    g2d.setFont(new Font("Arial", Font.BOLD, 24));
                    g2d.drawString("⚠️ WARNING: RISK DETECTED! FIX POSTURE", 20, 80);
                } else {
                    g2d.setColor(Color.GREEN);
                    g2d.drawString("STATUS: SAFE POSTURE", 20, 80);
                }
                // Clean up graphics rendering memory safely
                g2d.dispose();
                
                //Display the fully processed image frame onto the screen
                canvas.showImage(converter.convert(image));
            }
            
            // Close down resources gracefully when window shuts down
            grabber.stop();
            grabber.close();
            converter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
