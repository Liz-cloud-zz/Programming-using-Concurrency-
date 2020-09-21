//package FlowSkeleton;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class Water {
    Terrain t;
    int depth;
    BufferedImage water;
    ArrayList<int[]> blocks;

    /**
     * constructor for water
     * @param t Terrain Object
     * @param depth depth of water
     */
    public Water(Terrain t, int depth) {
        this.depth = depth;
        this.t = t;
        water=new BufferedImage(this.t.getDimX(),this.t.getDimY(), BufferedImage.TYPE_INT_ARGB);
        blocks=new ArrayList<>();

    }

    /**
     *  draw the box of water on the 3 by 3 area grid point
     * @param x coordinate x
     * @param y coordinate y
     */
    public void addWater(int x,int y){

        int[] coordinate=new int[2];
        coordinate[0]=x;
        coordinate[1]=y;
        blocks.add(coordinate);
        int pixel=water.getRGB(x,y);
        int blue=pixel&0xff;
        if(!(pixel==Color.blue.getRGB())){

            pixel=Color.blue.getRGB();
            int max_row=y+3;
            int max_column=x+3;
           for(int row=y-3;row<max_row+1;row++){
                for(int column=x-3;column<max_column+1;column++){
                    try {
                        this.t.height[row][column] += convertWater();
                        water.setRGB(column,row,pixel);
                    }catch (Exception e){break;}

                }
            }

        }
    }

    /**
     *  water unit corresponds to a depth of 0.01m so this depth is 0.03m
     * @return the depth of water in decimals
     */
    public float convertWater() {
        return (float) ((this.depth) * 0.01);
    }

    /**
     *   get the water surface at each point
     * @param x coordinate x
     * @param y coordinate y
     * @return floating value of water suface
     */
    public float getWater_surface(int x, int y) {
        return (t.getHeight(x, y) + convertWater());
    }

    /**
     * transfer water to the lowest point among the surrounding grid points from the current point
     * @param permu ArrayList to be processed by thread
     */
    public void transferWater(ArrayList permu) {
        int row = t.getDimY();
        int columns = t.getDimX();
        int[] coordinates = new int[2];

        Iterator iterator=permu.iterator();
        int index = (int) iterator.next();
        t.getPermute(index, coordinates);
        int blue = Color.blue.getRGB();
        int transparent = Color.TRANSLUCENT;
        int[] water_coordinates;

//check if there is water at that point on the terrain if there is water transfer water
        if(blocks.contains(coordinates)) {
            while (iterator.hasNext()) {
                int x = coordinates[0];
                int y = coordinates[1];
                float point = getWater_surface(x, y);
                int pixel = water.getRGB(x, y);

                float a, b, c, d, e, f, g, h, i;
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
                        if (((coordinates[0] - 1!= 0) & (coordinates[0] - 1 != columns - 1) & (coordinates[1] - 1!= 0) & (coordinates[1] - 1 != row - 1))) {
                            t.height[coordinates[1] - 1][coordinates[0] - 1] += 0.01;
                            water.setRGB(coordinates[0] - 1, coordinates[1] - 1, blue);
                        }
                    }
                } else if (((b < a) & (b < c) & (b < d) & (b < e) & (b < f) & (b < g) & (b < h))) {
                    if (b < point) {
                        t.height[y][x] -= 0.01;
                        water.setRGB(x, y, transparent);
                        if (((coordinates[0]  != 0) & (coordinates[0]  != columns - 1) & (coordinates[1] - 1 != 0) & (coordinates[1] - 1 != row - 1))){
                            t.height[coordinates[1] - 1][coordinates[0]] += 0.01;
                            water.setRGB(coordinates[0], coordinates[1] - 1, blue);
                    }

                    }
                } else if ((c < a) & (c < b) & (c < d) & (c < e) & (c < f) & (c < g) & (c < h)) {
                    if (c < point) {
                        t.height[y][x] -= 0.01;
                        water.setRGB(x, y, transparent);
                        if (((coordinates[0] +1!= 0) & (coordinates[0] + 1 != columns - 1) & (coordinates[1] - 1!= 0) & (coordinates[1] - 1 != row - 1))){
                            t.height[coordinates[1] - 1][coordinates[0] + 1] += 0.01;
                            water.setRGB(coordinates[0] + 1, coordinates[1] - 1, blue);
                        }
                    }
                } else if ((d < a) & (d < b) & (d < c) & (d < e) & (d < f) & (d < g) & (d < h)) {
                    if (d < point) {
                        t.height[y][x] -= 0.01;
                        water.setRGB(x, y, transparent);
                        if (((coordinates[0] -1!= 0) & (coordinates[0] -1 != columns - 1) & (coordinates[1] != 0) & (coordinates[1]  != row - 1))){
                        t.height[coordinates[1]][coordinates[0] - 1] += 0.01;
                        water.setRGB(coordinates[0] - 1, coordinates[1], blue);
                        }

                    }
                } else if ((e < a) & (e < b) & (e < c) & (e < d) & (e < f) & (e < g) & (e < h)) {
                    if (b < point) {
                        t.height[y][x] -= 0.01;
                        water.setRGB(x, y, transparent);
                        if (((coordinates[0] +1!= 0) & (coordinates[0] + 1 != columns - 1) & (coordinates[1] != 0) & (coordinates[1]  != row - 1))) {
                            t.height[coordinates[1]][coordinates[0] + 1] += 0.01;
                            water.setRGB(coordinates[0] + 1, coordinates[1], blue);
                        }
                    }
                } else if ((f < a) & (f < b) & (f < c) & (f < d) & (f < e) & (f < g) & (e < h)) {
                    if (f < point) {
                        t.height[y][x] -= 0.01;
                        water.setRGB(x, y, transparent);
                        if (((coordinates[0] -1!= 0) & (coordinates[0] -1 != columns - 1) & (coordinates[1] +1!= 0) & (coordinates[1] +1 != row - 1))) {
                            t.height[coordinates[1] + 1][coordinates[0] - 1] += 0.01;
                            water.setRGB(coordinates[0] - 1, coordinates[1] + 1, blue);
                        }
                    }
                } else if ((g < a) & (g < b) & (g < c) & (g < d) & (g < e) & (g < f) & (g < h)) {
                    if (g < point) {
                        t.height[y][x] -= 0.01;
                        water.setRGB(x, y, transparent);
                        if (((coordinates[0] != 0) & (coordinates[0] != columns - 1) & (coordinates[1] + 1!= 0) & (coordinates[1] +1 != row - 1))) {
                            t.height[coordinates[1] + 1][coordinates[0]] += 0.01;
                            water.setRGB(coordinates[0], coordinates[1] + 1, blue);
                        }

                    }
                } else if ((h < a) & (h < b) & (h < c) & (h < d) & (h < e) & (h < f) & (h < g)) {
                    if (h < point) {
                        t.height[y][x] -= 0.01;
                        water.setRGB(x, y, transparent);
                        if (((coordinates[0] +1!= 0) & (coordinates[0] + 1 != columns - 1) & (coordinates[1] + 1!= 0) & (coordinates[1] + 1 != row - 1))) {
                            t.height[coordinates[1] + 1][coordinates[0] + 1] += 0.01;
                            water.setRGB(coordinates[0] + 1, coordinates[1] + 1, blue);
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @return the water image
     */
    public BufferedImage getImg(){return water;}

    /**
     * remove water from the terrain
     */

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