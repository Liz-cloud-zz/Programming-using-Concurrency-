//package FlowSkeleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JPanel;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.atomic.*;

public class Producer implements Runnable{
    private int threadnum;
    private Water w;
    private Thread thread;

    /**
     * Producer constructor initializes producer object
     * @param num Thread number
     * @param w Water object
     */
   public Producer(Water w, int num) {
        this.w = w;
        this.threadnum = num;
        w.t.genPermute();

    }

    /**
     *  split the permute array in 4 to assign each 1/4 to the thread
     */
    public synchronized void run() {
            int quarter = (w.t.permute.size()) / 4;
            ArrayList<Integer> first = new ArrayList();
            for (int index0 = 0; index0 < quarter - 1; index0++) {
                first.add(w.t.permute.get(index0));
            }
            ArrayList<Integer> second = new ArrayList();
            for (int index1 = quarter; index1 < (quarter * 2) - 1; index1++) {
                second.add(w.t.permute.get(index1));
            }
            ArrayList<Integer> third = new ArrayList();
            for (int index2 = (quarter * 2); index2 < (quarter * 3) - 1; index2++) {
                third.add(w.t.permute.get(index2));
            }
            ArrayList<Integer> fourth = new ArrayList();
            for (int index3 = (quarter * 3); index3 < w.t.permute.size(); index3++) {
                fourth.add(w.t.permute.get(index3));
            }

            //split the data between two threads
            if (threadnum == 0) {
                w.transferWater(first);
            } else if (threadnum == 1) {
                w.transferWater(second);
            } else if (threadnum == 2) {
                w.transferWater(third);
            } else if (threadnum == 3) {
                w.transferWater(fourth);
            }

        }

    }


