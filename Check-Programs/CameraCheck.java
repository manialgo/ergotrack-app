// Camera check file


package com.ergotrack;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class CameraCheck {
    public static void main(String[] args) {
        // 1. Create a grabber object targeting your default webcam (0)
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        
        try {
            grabber.start(); // Open the webcam
            
            // 2. Create a simple window window titled "ErgoTrack Window"
            CanvasFrame canvas = new CanvasFrame("ErgoTrack Demo");
            canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            
            // 3. A simple while loop (just like a LeetCode pointer problem!)
            while (canvas.isVisible()) {
                // Grab a single frame from the webcam continuous stream
                Frame frame = grabber.grab();
                
                if (frame != null) {
                    // Display that frame in our window window
                    canvas.showImage(frame);
                }
            }
            
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}