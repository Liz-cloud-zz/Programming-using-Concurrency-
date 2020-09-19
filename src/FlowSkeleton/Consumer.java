package FlowSkeleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import javax.swing.JPanel;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.atomic.*;

public class Consumer extends Thread {

    Producer p;
        public Consumer(Producer p){
          this.p=p;
        }
        public void run(){
            while (p.stop.get()){
                synchronized (p.w){
                        try{
                            p.w.wait();
                        } catch (Exception ex) {ex.printStackTrace();}
                    }
                }
        }
}

