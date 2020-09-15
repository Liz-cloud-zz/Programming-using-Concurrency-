
package FlowSkeleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.swing.JPanel;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;


public class FlowPanel extends JPanel implements Runnable {

//	Terrain land;
	Water water;
	private volatile boolean running = true;
	private volatile boolean paused = false;
	private final Object pauseLock = new Object();

	FlowPanel(Water water){
		this.water=water;
	}

	// responsible for painting the terrain and water
	// as images
	@Override
    protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		super.paintComponent(g);
		// draw the landscape in greyscale as an image
		if (water.t.getImage()!= null) {
			g.drawImage(water.t.getImage(), 0, 0, null);
			g.drawImage(water.getImg(),0,0,null);
			}
		}

// end the transfer of water
	public synchronized void end() {
			running = false;
			// you might also want to interrupt() the Thread that is
			// running this Runnable, too, or perhaps call:
			unpause();
			// to unblock
		}

	//pause the transfer of water
	public void pause() {
	// you may want to throw an IllegalStateException if !running
	paused = true;
	}
	//resume the transfer of water
	public synchronized void unpause() {
		synchronized (pauseLock) {
		paused = false;
		pauseLock.notifyAll(); // Unblocks thread
		}
	}

//transfers water to lowest point
	public void run() {
		while (running) {
			synchronized (pauseLock) {
				if (!running) { // may have changed while waiting to
					// synchronize on pauseLock
					break;
				}
				if (paused) {
					try {
						synchronized (pauseLock) {
							pauseLock.wait(); // will cause this Thread to block until// another thread calls pauseLock.notifyAll()
							// Note that calling wait() will// relinquish the synchronized lock that this
							// thread holds on pauseLock so another thread	// can acquire the lock to call notifyAll()
						}
					} catch (InterruptedException ex) {
						break;
					}
					if (!running) { // running might have changed since we paused
						break;
					}
				}
			}
			// Your code here
			water.transferWater();
		}
		repaint();
		// display loop here
		// to do: this should be controlled by the GUI
		// to allow stopping and starting
	}
}