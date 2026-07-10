// Camera check file

package com.ergotrack;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class CameraCheck {

	public static void main(String[] args) {
		
        //Create a grabber object targeting your default webcam (0)
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        
        //Create a simple window window titled "ErgoTrack Window"        
        CanvasFrame canvas = new CanvasFrame("ErgoTrack Demo");
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        //Initialize the object for Frame as frame
        Frame frame = new Frame();
        
        try {
        		// Open the webcam
            grabber.start();
            
            //While loop for infinite run that until the canvas frame is closed by the user
            while (canvas.isVisible()) {
            	
                // Grab a single frame from the webcam continuous stream
            		frame = grabber.grab();
                
                // Display that frame in our window window
            		if (frame != null) canvas.showImage(frame);
            		
            }
            
            grabber.stop();
            grabber.close();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
	
}
