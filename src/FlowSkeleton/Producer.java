package FlowSkeleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import javax.swing.JPanel;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.atomic.*;

public class Producer extends Thread{
    private int threadnum;
    AtomicBoolean stop;
    Water w;

    public Producer(Water w ,int num){
        this.w=w;
        this.threadnum=num;
        stop=new AtomicBoolean(false);

    }
    public void run(){
        synchronized (w){
            while(!stop.get()){
                try {
                    w.transferWater(threadnum);
//                Thread.sleep(10);  // milliseconds
                 } catch (Exception ex) {ex.printStackTrace();}
            }
            w.notify();
        }


    }
//    public void start(){
//        if(thread==null){
//            thread=new Thread(this,this.threadName);
//            thread.start();
//        }
//    }
//    public synchronized void flow(){
//        while(!stop.get()){
//            try {
//                wait();
//            }catch (InterruptedException e){}
//            stop.set(false);
//            w.transferWater();
//        }
//    }
//    public synchronized void paused(){
//        while (stop.get()){
//            try {
//                wait();
//            }catch (InterruptedException e){}
//            stop.set(true);
//            notify();
//        }
//    }
}
