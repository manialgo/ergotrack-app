package com.ergotrack;

import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;

public class TrialForCamera{

	public static void main(String[] args) {

		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		
		CanvasFrame canvas = new CanvasFrame("Canvas Frame Created!");
		canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		
		Frame frame = new Frame();
		
		try {
			grabber.start();
			
			while(canvas.isVisible()) {
				frame = grabber.grab();

				if(frame != null) canvas.showImage(frame);
			}
			grabber.stop();
			grabber.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
