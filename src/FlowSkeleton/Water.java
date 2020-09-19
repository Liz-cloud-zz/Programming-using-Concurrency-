package FlowSkeleton;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
public class Water {
    Terrain t;
    int depth;
    BufferedImage water;
    public Water(Terrain t, int depth) {
        this.depth = depth;
        this.t = t;
        water=new BufferedImage(this.t.getDimX(),this.t.getDimY(), BufferedImage.TYPE_INT_ARGB);
        t.genPermute();
    }

    //draw the box on the grid point
    public void addWater(int x,int y){
        int pixel=water.getRGB(x,y);
        int blue=pixel&0xff;
        if(!(pixel==Color.blue.getRGB())){
            pixel=Color.blue.getRGB();
            int max_row=y+3;
            int max_column=x+3;
//            if(!((y-3<=0)||(x-3<=0)||(max_column<t.getDimX()-1)||(max_row<t.getDimY()-1))){
                for(int row=y-3;row<max_row+1;row++){
                    for(int column=x-3;column<max_column+1;column++){
                        this.t.height[row][column] += convertWater();
                        water.setRGB(column,row,pixel);
                    }
                }
//            }

        }
    }

    // water unit corresponds to a depth of 0.01m so this depth is 0.03m
    public float convertWater() {
        return (float) ((this.depth) * 0.01);
    }

    //get the water surface at each point
    public float getWater_surface(int x, int y) {
        return (t.getHeight(x, y) + convertWater());
    }

//    //transfer water to the lowest point among the surrounding grid points from the current point
    public void transferWater(int thread) {
        int row = t.getDimY();
        int columns = t.getDimX();
        int [] coordinates = new int[2];
        int half=(t.permute.size())/2;
        Iterator i = null;
        ArrayList<Integer> first=new ArrayList();
        for(int index=0;index<half;index++){
            first.add(t.permute.get(index));
        }
        ArrayList<Integer> second=new ArrayList();
        for(int in=0;in<half;in++){
            second.add(t.permute.get(in));
        }
        //split the data between two threads
        if(thread==1){
            i=first.iterator();
        }
        else if(thread==2){
            i=second.iterator();
        }
        int  blue=Color.blue.getRGB();
        int transparent=Color.TRANSLUCENT;
        while (i.hasNext()) {
                int index = (int) i.next();
                t.getPermute(index, coordinates);
                int x=coordinates[0];
                int y=coordinates[1];
                float point = getWater_surface(x, y);
                int pixel=water.getRGB(x,y);
//check if there is water at that point on the terrain if there is water transfer water
            if(pixel==blue){
                    if (!((x == 0) | (x == columns - 1) | (y == 0) | (y == row - 1))) {
                        float a, b, c, d, e, f, g, h;
                        a = getWater_surface(coordinates[0] - 1, coordinates[1] - 1);
                        b = getWater_surface(coordinates[0], coordinates[1] - 1);
                        c = getWater_surface(coordinates[0] + 1, coordinates[1] - 1);
                        d = getWater_surface(coordinates[0] - 1, coordinates[1]);
                        e = getWater_surface(coordinates[0] + 1, coordinates[1]);
                        f = getWater_surface(coordinates[0] - 1, coordinates[1] + 1);
                        g = getWater_surface(coordinates[0], coordinates[1] + 1);
                        h = getWater_surface(coordinates[0] + 1, coordinates[1] + 1);

                        //The current water_surface at (x,y) is compared to the water surface of the neighbouring grid positions. A
                        //single unit of water is transferred to the lowest of these neighbours, so long as the
                        //water surface of this neighbour is strictly lower than that of the current grid position.
                        //Otherwise no water is transferred out of the current grid position.
                        if ((a < b) & (a < c) & (a < d) & (a < e) & (a < f) & (a < g) & (a < h)) {
                            if (a < point) {
                                t.height[y][x] -= 0.01;
                                water.setRGB(x, y, transparent);
                                t.height[coordinates[1] - 1][coordinates[0] - 1] += 0.01;
                                water.setRGB(coordinates[0] - 1, coordinates[1] - 1, blue);
                            }
                        } else if (((b < a) & (b < c) & (b < d) & (b < e) & (b < f) & (b < g) & (b < h))) {
                            if (b < point) {
                                t.height[y][x] -= 0.01;
                                water.setRGB(x, y, transparent);
                                t.height[coordinates[1] - 1][coordinates[0]] += 0.01;
                                water.setRGB(coordinates[0], coordinates[1] - 1, blue);
                            }
                        } else if ((c < a) & (c < b) & (c < d) & (c < e) & (c < f) & (c < g) & (c < h)) {
                            if (c < point) {
                                t.height[y][x] -= 0.01;
                                water.setRGB(x, y, transparent);
                                t.height[coordinates[1] - 1][coordinates[0] + 1] += 0.01;
                                water.setRGB(coordinates[0]+1, coordinates[1] - 1, blue);
                            }
                        } else if ((d < a) & (d < b) & (d < c) & (d < e) & (d < f) & (d < g) & (d < h)) {
                            if (d < point) {
                                t.height[y][x] -= 0.01;
                                water.setRGB(x, y, transparent);
                                t.height[coordinates[1]][coordinates[0] - 1] += 0.01;
                                water.setRGB(coordinates[0]-1, coordinates[1] , blue);
                            }
                        } else if ((e < a) & (e < b) & (e < c) & (e < d) & (e < f) & (e < g) & (e < h)) {
                            if (b < point) {
                                t.height[y][x] -= 0.01;
                                water.setRGB(x, y, transparent);
                                t.height[coordinates[1]][coordinates[0] + 1] += 0.01;
                                water.setRGB(coordinates[0]+1, coordinates[1] , blue);
                            }
                        } else if ((f < a) & (f < b) & (f < c) & (f < d) & (f < e) & (f < g) & (e < h)) {
                            if (f < point) {
                                t.height[y][x] -= 0.01;
                                water.setRGB(x, y, transparent);
                                t.height[coordinates[1] + 1][coordinates[0] - 1] += 0.01;
                                water.setRGB(coordinates[0]-1, coordinates[1] + 1, blue);
                            }
                        } else if ((g < a) & (g < b) & (g < c) & (g < d) & (g < e) & (g < f) & (g < h)) {
                            if (g < point) {
                                t.height[y][x] -= 0.01;
                                water.setRGB(x, y, transparent);
                                t.height[coordinates[1] + 1][coordinates[0]] += 0.01;
                                water.setRGB(coordinates[0], coordinates[1] + 1, blue);
                            }
                        } else if ((h < a) & (h < b) & (h < c) & (h < d) & (h < e) & (h < f) & (h < g)) {
                            if (h < point) {
                                t.height[y][x] -= 0.01;
                                water.setRGB(x, y, transparent);
                                t.height[coordinates[1] + 1][coordinates[0] + 1] += 0.01;
                                water.setRGB(coordinates[0]+1, coordinates[1] + 1, blue);
                            }
                        }
                    }
                }
            }
        }

//return the water image
    public BufferedImage getImg(){return water;}

    //remove water from the terrain
    public void removeWater(){
        for(int y=0;y<water.getHeight();y++){
            for(int x=0;x<water.getWidth();x++){
                int p=water.getRGB(x,y);
                p=Color.TRANSLUCENT;
                water.setRGB(x,y,p);
            }
        }
    }
}