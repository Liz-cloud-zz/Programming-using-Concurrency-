
package FlowSkeleton;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.util.Scanner;

public class Flow {
	static long startTime = 0;
	static int frameX;
	static int frameY;
	static FlowPanel fp;

	// start timer
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	// stop timer, return time elapsed in seconds
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}
	
	public static void setupGUI(int frameX,int frameY,Terrain landdata) {
		
		Dimension fsize = new Dimension(800, 800);
    	JFrame frame = new JFrame("Waterflow"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.getContentPane().setLayout(new BorderLayout());
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
   		Water w=new Water(landdata,3);
		fp = new FlowPanel(w);
		fp.setPreferredSize(new Dimension(frameX,frameY));
		g.add(fp);
	    
		// to do: add a MouseListener, buttons and ActionListeners on those buttons
		JPanel b = new JPanel();
	    b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
		JButton endB = new JButton("End");;
		// add the listener to the jbutton to handle the "pressed" event
		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				frame.dispose();
			}
		});
		//add MouseLister, buttons "play"
		JButton reset = new JButton("Reset");;
		// add the listener to the jbutton to handle the "pressed" event
		reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				w.removeWater();
			}
		});
		//add MouseLister, buttons "pause"
		JButton pause = new JButton("Pause");;
		// add the listener to the jbutton to handle the "pressed" event
		pause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				fp.atomicBoolean.set(true);
				fp.run();
			}
		});
		//add MouseLister, buttons "play"
		JButton play = new JButton("Play");;
		// add the listener to the jbutton to handle the "pressed" event
		play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				fp.run();
			}
		});
		fp.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int row=e.getY();
				int column=e.getX();
					w.addWater(row-12,column-12);
			}
		});

		b.add(reset);
		b.add(pause);
		b.add(play);
		b.add(endB);
		g.add(b);
    	
		frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
      	frame.setLocationRelativeTo(null);  // center window on screen
      	frame.add(g); //add contents to window
        frame.setContentPane(g);
        frame.setVisible(true);
        Thread fpt = new Thread(fp);
        fpt.start();
	}
	
		
	public static void main(String[] args) {
		Terrain landdata = new Terrain();
		
		// check that number of command line arguments is correct
		if(args.length != 1)
		{
			System.out.println("Incorrect number of command line arguments. Should have form: java -jar flow.java intputfilename");
			System.exit(0);
		}
		// landscape information from file supplied as argument
		landdata.readData(args[0]);
		frameX = landdata.getDimX();
		frameY = landdata.getDimY();
		SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata));
		
		// to do: initialise and start simulation
	}
}
