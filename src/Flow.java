
//package FlowSkeleton;
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

	/**
	 * // start timer
	 */

	private static void tick(){
		startTime = System.currentTimeMillis();
	}

	/**
	 * // stop timer
	 * @return return time elapsed in second
	 */
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}

	/**
	 * set up the GUI and creates Action Listeners and Mouse listeners to effect changes on GUI window
	 * @param frameX width of terrain
	 * @param frameY height of terrain
	 * @param landdata terrain object
	 */
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
		fp.end.set(true);
//		Thread fpt=new Thread(fp);
//		fpt.start();

		// to do: add a MouseListener, buttons and ActionListeners on those buttons
		JPanel b = new JPanel();
	    b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
		JLabel timestep;

		fp.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				int row = e.getY();
				int column = e.getX();
				if ((column != 0)&(column != landdata.getDimX() - 1) & (row != 0) || (row != landdata.getDimY() - 1)) {
					w.addWater(column, row);

					fp.repaint();
				}
			}
		});

		JButton endB = new JButton("End");;
		// add the listener to the jbutton to handle the "pressed" event
		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				frame.dispose();
			}
		});

		//add MouseLister, buttons "reset"
		JButton reset = new JButton("Reset");;
		// add the listener to the jbutton to handle the "pressed" event
		reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				w.removeWater();
				fp.repaint();
			}
		});

		//add MouseLister, buttons "play"
		JButton play = new JButton("Play");;
		// add the listener to the jbutton to handle the "pressed" event
		play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// to do ask threads to stop

				fp.running.set(true);

			}

		});
		//add MouseLister, buttons "pause"
		JButton pause = new JButton("Pause");;
		// add the listener to the jbutton to handle the "pressed" event
		pause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop

				fp.running.set(false);
			}
			});

		timestep=new JLabel("Time step: "+String.valueOf(fp.getTimeStep()));
		b.add(reset);
		b.add(pause);
		b.add(play);
		b.add(endB);
		b.add(timestep);

		g.add(b);
		frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
      	frame.setLocationRelativeTo(null);  // center window on screen
      	frame.add(g); //add contents to window
        frame.setContentPane(g);
        frame.setVisible(true);

	}

	/**
	 * //main method passses in the textfile of terrain heights and dimension as argument and creates a terrain image and calls the setupGUI method
	 * @param args textfile of terrain data
	 */

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
