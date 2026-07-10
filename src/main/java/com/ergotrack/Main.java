package com.ergotrack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_dnn.Net;


import static org.bytedeco.opencv.global.opencv_core.minMaxLoc;
import static org.bytedeco.opencv.global.opencv_dnn.blobFromImage;
import static org.bytedeco.opencv.global.opencv_dnn.readNetFromCaffe;

public class Main {
    public static void main(String[] args) {
        String protoPath = "model/pose_deploy_linevec_faster_4_stages.prototxt";
        String weightsPath = "model/pose_iter_160000.caffemodel";

        System.out.println("Loading Neural Network into memory...");
        Net net = readNetFromCaffe(protoPath, weightsPath);
        System.out.println("Neural Network Loaded Successfully!");

        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        
        // Native Mat converter instance
        OpenCVFrameConverter.ToMat matConverter = new OpenCVFrameConverter.ToMat();

        try {
            grabber.start();
            CanvasFrame canvas = new CanvasFrame("Ergo-Track AI Safety Monitor");
            canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

            while (canvas.isVisible()) {
                Frame rawFrame = grabber.grab();
                if (rawFrame == null) continue;

                // Safely convert the frame to an OpenCV Mat object
                Mat rawMat = matConverter.convert(rawFrame);
                if (rawMat == null || rawMat.empty()) continue;

                BufferedImage image = converter.convert(rawFrame);
                if (image == null) continue;

                int frameW = image.getWidth();
                int frameH = image.getHeight();

                // Replace 'null' with explicit default Scalar values
                Scalar mean = new Scalar(0, 0, 0, 0); 
                Mat blob = blobFromImage(rawMat, 1.0 / 255.0, new Size(368, 368), 
                                        mean, false, false, org.bytedeco.opencv.global.opencv_core.CV_32F);
                net.setInput(blob);

                Mat outputMatrix = net.forward(); 

                int outputPointsCount = outputMatrix.size(1); 
                int outH = outputMatrix.size(2);
                int outW = outputMatrix.size(3);

                com.ergotrack.ErgoMath.Point shoulderPt = null;
                com.ergotrack.ErgoMath.Point hipPt = null;
                com.ergotrack.ErgoMath.Point kneePt = null;

                for (int i = 0; i < outputPointsCount; i++) {
                    Mat probMap = outputMatrix.row(0).col(i).reshape(1, outH);
                    
                    double[] maxVal = new double[1];
                    Point maxLoc = new Point();
                    minMaxLoc(probMap, null, maxVal, null, maxLoc, null);

                    if (maxVal[0] > 0.1) {
                        int x = (int) ((frameW * maxLoc.x()) / outW);
                        int y = (int) ((frameH * maxLoc.y()) / outH);

                        if (i == 2) shoulderPt = new com.ergotrack.ErgoMath.Point(x, y);
                        if (i == 8) hipPt = new com.ergotrack.ErgoMath.Point(x, y);
                        if (i == 9) kneePt = new com.ergotrack.ErgoMath.Point(x, y);
                    }
                }

                Graphics2D g2d = image.createGraphics();
                g2d.setStroke(new java.awt.BasicStroke(4f));

                if (shoulderPt != null && hipPt != null && kneePt != null) {
                    double liveAngle = ErgoMath.calculateAngle(shoulderPt, hipPt, kneePt);
                    boolean isRisk = (liveAngle < 145.0);

                    g2d.setColor(isRisk ? Color.RED : Color.GREEN);
                    g2d.drawLine((int)shoulderPt.x, (int)shoulderPt.y, (int)hipPt.x, (int)hipPt.y);
                    g2d.drawLine((int)hipPt.x, (int)hipPt.y, (int)kneePt.x, (int)kneePt.y);

                    g2d.setFont(new Font("Arial", Font.BOLD, 20));
                    g2d.setColor(Color.WHITE);
                    g2d.drawString("Live Hip Angle: " + String.format("%.1f", liveAngle) + "°", 20, 40);

                    if (isRisk) {
                        g2d.setColor(Color.RED);
                        g2d.setFont(new Font("Arial", Font.BOLD, 24));
                        g2d.drawString("⚠️ WARNING: FIX POSTURE!", 20, 80);
                    } else {
                        g2d.setColor(Color.GREEN);
                        g2d.drawString("STATUS: SAFE POSTURE", 20, 80);
                    }
                } else {
                    g2d.setFont(new Font("Arial", Font.BOLD, 18));
                    g2d.setColor(Color.ORANGE);
                    g2d.drawString("Searching for body landmarks... Please step back.", 20, 40);
                }

                g2d.dispose();
                canvas.showImage(converter.convert(image));
            }

            grabber.stop();
            grabber.close();
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        converter.close();
        matConverter.close();
    }
}
