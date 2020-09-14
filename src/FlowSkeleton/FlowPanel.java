
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
//	private List<Water> blocks=new LinkedList<Water>();
	AtomicBoolean atomicBoolean=new AtomicBoolean();

//	FlowPanel(Terrain terrain) {
//		land=terrain;
//	}
	FlowPanel(Water water){
		this.water=water;
	}

//	//add block of water of the mouseclick point in grid
//	public void addBlock(Water w){
//		blocks.add(w);
//		this.repaint();
//	}
//	public void removeWater(){
////		blocks.removeAll(this.blocks);
////		this.repaint();
//		int transparent=(0<<24) | (0<<16) | (0<<8) | (0);
//		for(int y=0;y<water.getImg().getHeight();y++){
//			for(int x=0;x<water.getImg().getWidth();x++){
//				water.getImg().setRGB(x,y,transparent);
//			}
//		}
//	}

	// responsible for painting the terrain and water
	// as images
	@Override
    protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		super.paintComponent(g);
		// draw the landscape in greyscale as an image
		if (water.getImg()!= null) {
			g.drawImage(water.getImg(), 0, 0, null);
			}
		}
	public void run() {
		boolean flag=atomicBoolean.get();
		while (flag==false){
			water.transferWater();
		}
		// display loop here
		// to do: this should be controlled by the GUI
		// to allow stopping and starting

	}
}