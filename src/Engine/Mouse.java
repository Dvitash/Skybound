package Engine;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.event.MouseMotionListener;

public class Mouse {

    // tracks the state of the mouse, true pressed, false released
    private static boolean mouseState = false;

    // variable for cursors position
    private static Point cursorPoint = new Point(0, 0);

    // adds the  MotionListener add its methods
    private static final MouseMotionListener MouseTracker = new MouseMotionListener() {

        @Override // unused method
        public void mouseDragged(MouseEvent e) {

        }

        @Override // tracks the location of the cursor
        public void mouseMoved(MouseEvent e) {
            cursorPoint = e.getPoint();

        }

    };

    // adds the mouseListener and its methods
    private static final MouseListener MouseListener = new MouseListener() {

        @Override //unused
        public void mouseClicked(MouseEvent e) {

        }

        @Override // allows the player to shoot on a click by changing mouseSate
        public void mousePressed(MouseEvent e) {
            mouseState = true;

        }

        @Override // set mouseState back to false to stop the player from shooting
        public void mouseReleased(MouseEvent e) {
            mouseState = false;
        }

        @Override // unused
        public void mouseEntered(MouseEvent e) {

        }

        @Override // unused
        public void mouseExited(MouseEvent e) {

        }

    };

    // makes the class almost static
    private Mouse() {
    };

    // allows other classes to access the motionListener
    public static MouseMotionListener getMouseMotionListener() {
        return MouseTracker;
    }

    // allows other classes to access the mouseListener
    public static MouseListener getMouseListener() {
        return MouseListener;
    }

    // gives access to the state of the mouse
    public static boolean isMouseClicked() {
        return mouseState;
    }

}
