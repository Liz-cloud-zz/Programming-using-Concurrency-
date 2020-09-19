
package FlowSkeleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.swing.JPanel;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.atomic.*;

public class FlowPanel extends JPanel implements Runnable {
	Water water;
	AtomicBoolean running;
	AtomicBoolean paused;
	private final Object pauseLock;
	private volatile AtomicInteger atomicInteger;
	private final AtomicBoolean lock;

	FlowPanel(Water water) {
		this.water = water;
		atomicInteger = new AtomicInteger(0);
		lock = new AtomicBoolean(false);
		pauseLock = new Object();
		running=new AtomicBoolean(false);
		paused=new AtomicBoolean(false);
	}
	// responsible for painting the terrain and water
	// as images
	@Override
	protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		super.paintComponent(g);
		// draw the landscape in greyscale as an image
		if (water.t.getImage() != null) {
			g.drawImage(water.t.getImage(), 0, 0, null);
			g.drawImage(water.getImg(), 0, 0, null);
		}
	}

	// end the transfer of water
	public void end() {
		running.set(false);
		unpause();
		// you might also want to interrupt() the Thread that is
		// running this Runnable, too, or perhaps call:
	}

	//pause the transfer of water
	public void pause() {
		// you may want to throw an IllegalStateException if !running
		paused.set(true);
	}

	//resume the transfer of water
	public void unpause() {
		running.set(true);
		paused.set(false);
		unlock();

	}
	//locks the lock
	public void step(){
		if(lock.get()==true)synchronized (pauseLock){
			try{
				pauseLock.wait();
			}catch (InterruptedException ex){
			}
		}
	}
	//sets lock variable to true
	public void lock(){
		lock.set(true);
	}
	//unlocks the lock
	public void unlock(){
		lock.set(false);
		synchronized (pauseLock){
			pauseLock.notify();
		}
	}
//returns the time step of the water transfer simulation
	public int getTimeStep() {
		return atomicInteger.get();
	}

	@Override
	public void run() {
		while (running.get() == true) {
			synchronized (pauseLock) {
				if (!running.get()) {
					break;
				}
				if (paused.get() == true) {
					lock();
					step();
				}
				if (!running.get()) {
					break;
				}
			}
			water.transferWater();
			atomicInteger.incrementAndGet();
		}
		repaint();
	}
//@Override
//public void run() {
//	while (running.get()) {
//		synchronized (pauseLock) {
//			if (!running.get()) { // may have changed while waiting to
//				// synchronize on pauseLock
//				break;
//			}
//			if (paused.get()) {
//				try {
//					synchronized (pauseLock) {
//						pauseLock.wait(); // will cause this Thread to block until
//						// another thread calls pauseLock.notifyAll()
//						// Note that calling wait() will
//						// relinquish the synchronized lock that this
//						// thread holds on pauseLock so another thread
//						// can acquire the lock to call notifyAll()
//						// (link with explanation below this code)
//					}
//				} catch (InterruptedException ex) {
//					break;
//				}
//				if (!running.get()) { // running might have changed since we paused
//					break;
//				}
//			}
//		}
//		water.transferWater();
//		atomicInteger.incrementAndGet();
//		// Your code here
//	}
//	repaint();
//}
}


