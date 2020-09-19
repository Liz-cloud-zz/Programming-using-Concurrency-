package FlowSkeleton;

import java.util.concurrent.atomic.AtomicBoolean;

public class SecondProducer extends Thread {
    private int threadnum;
    AtomicBoolean stop;
    Water w;

    public SecondProducer(Water w, int num) {
        this.w = w;
        this.threadnum = num;
        stop = new AtomicBoolean(false);
    }

    public void run() {
        synchronized (w) {
            while (!stop.get()) {
                try {
                    w.transferWater(threadnum);
//                Thread.sleep(10);  // milliseconds
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            w.notify();
        }
    }
}
