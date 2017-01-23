package com.arnaudbos.java2d;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class AffineTransformZoomExample {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JComponent canvas = new ZoomCanvas();
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.setSize(600, 640);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        frame.setVisible(true);
    }

    private static class ZoomCanvas extends JComponent {

        ZoomCanvas() {
            setOpaque(true);
            setDoubleBuffered(true);
        }

        public void paint(Graphics g) {

            Graphics2D ourGraphics = (Graphics2D) g;
            ourGraphics.setColor(Color.white);
            ourGraphics.fillRect(0, 0, getWidth(), getHeight());
            ourGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            ourGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            ourGraphics.setColor(Color.black);
            ourGraphics.drawRect(100, 100, 100, 100);

            // Create matrix (set to identity by default)
            AffineTransform tx = new AffineTransform();

            // This is not the transformation you're looking for
            tx.translate(-150, -150);
            tx.scale(2, 2);
            tx.translate(150, 150);
            ourGraphics.setTransform(tx);
            ourGraphics.setColor(Color.red);
            ourGraphics.drawRect(100, 100, 100, 100);

            // Reset matrix to identity to clear previous transformations
            tx.setToIdentity();

            // Apply our transformations in order to zoom-in the square
            tx.preConcatenate(AffineTransform.getTranslateInstance(-150, -150));
            tx.preConcatenate(AffineTransform.getScaleInstance(2, 2));
            tx.preConcatenate(AffineTransform.getTranslateInstance(150, 150));
            ourGraphics.setTransform(tx);
            ourGraphics.setColor(Color.green);
            ourGraphics.drawRect(100, 100, 100, 100);
        }
    }
}