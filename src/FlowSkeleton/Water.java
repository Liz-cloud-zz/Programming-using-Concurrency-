package FlowSkeleton;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Scanner;
public class Water {
    Terrain t;
    int depth;
    BufferedImage img;
    public Water(Terrain t, int depth) {
        this.depth = depth;
        this.t = t;
        img=this.t.getImage();
    }

    //draw the box on the grid point
    public void addWater(int x,int y){

        if(!((x== 0) || (y == img.getWidth()- 1) ||(y == 0)|| (y == img.getHeight()- 1))){
            int pixel=img.getRGB(x,y);
            int blue=pixel&0xff;
            pixel=(0<<24) | (0<<16) | (0<<8) | (blue);
            int max_row=y+3;
            int max_column=x+3;
            for(int row=y-3;row<max_row+1;row++){
                for(int column=x-3;column<max_column+1;column++){
                    this.t.height[row][column] += convertWater();
                }
            }
            img.setRGB(x,y,pixel);
        }

    }
    //get the water surface at each point
    public float getWater_surface(int x, int y) {
        return (t.getHeight(x, y) + convertWater());
    }
    // water unit corresponds to a depth of 0.01m so this depth is 0.03m
    public float convertWater() {
        return (float) ((this.depth) * 0.01);
    }

//    //transfer water to the lowest point among the surrounding grid points from the current point
    public void transferWater() {
        int row = img.getHeight();
        int columns = img.getWidth();
        int [] coordinates = new int[2];
        Iterator i =t.permute.iterator();

    for(int y=1; y < row-1; y++)
        for(int x=1; x < columns-1; x++) {
            float point = getWater_surface(x, y);
            int pixel=img.getRGB(x,y);
            Color color=new Color(pixel);
            int  blue=(0<<24) | (0<<16) | (0<<8) | (pixel&0xff);
            int alpha =(pixel>>24)&0xff;
            int transparent=(alpha) | (0<<16) | (0<<8) | (0);
            if(color.equals(Color.BLUE)){
                while (i.hasNext()) {
                    int index = (int) i.next();
                    t.getPermute(index, coordinates);
                    if (!((coordinates[0] == 0) | (coordinates[0] == columns - 1) | (coordinates[1]  == 0) | (coordinates[1] == row - 1))) {
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
                                img.setRGB(x, y, transparent);
                                t.height[coordinates[1] - 1][coordinates[0] - 1] += 0.01;
                                img.setRGB(coordinates[0] - 1, coordinates[1] - 1, transparent);
                            }
                        } else if (((b < a) & (b < c) & (b < d) & (b < e) & (b < f) & (b < g) & (b < h))) {
                            if (b < point) {
                                t.height[y][x] -= 0.01;
                                img.setRGB(x, y, transparent);
                                t.height[coordinates[1] - 1][coordinates[0]] += 0.01;
                                img.setRGB(coordinates[0], coordinates[1] - 1, blue);
                            }
                        } else if ((c < a) & (c < b) & (c < d) & (c < e) & (c < f) & (c < g) & (c < h)) {
                            if (c < point) {
                                t.height[y][x] -= 0.01;
                                img.setRGB(x, y, transparent);
                                t.height[coordinates[1] - 1][coordinates[0] + 1] += 0.01;
                                img.setRGB(coordinates[0]+1, coordinates[1] - 1, blue);
                            }
                        } else if ((d < a) & (d < b) & (d < c) & (d < e) & (d < f) & (d < g) & (d < h)) {
                            if (d < point) {
                                t.height[y][x] -= 0.01;
                                img.setRGB(x, y, transparent);
                                t.height[coordinates[1]][coordinates[0] - 1] += 0.01;
                                img.setRGB(coordinates[0]-1, coordinates[1] , blue);
                            }
                        } else if ((e < a) & (e < b) & (e < c) & (e < d) & (e < f) & (e < g) & (e < h)) {
                            if (b < point) {
                                t.height[y][x] -= 0.01;
                                img.setRGB(x, y, transparent);
                                t.height[coordinates[1]][coordinates[0] + 1] += 0.01;
                                img.setRGB(coordinates[0]+1, coordinates[1] , blue);
                            }
                        } else if ((f < a) & (f < b) & (f < c) & (f < d) & (f < e) & (f < g) & (e < h)) {
                            if (f < point) {
                                t.height[y][x] -= 0.01;
                                img.setRGB(x, y, transparent);
                                t.height[coordinates[1] + 1][coordinates[0] - 1] += 0.01;
                                img.setRGB(coordinates[0]-1, coordinates[1] + 1, blue);

                            }
                        } else if ((g < a) & (g < b) & (g < c) & (g < d) & (g < e) & (g < f) & (g < h)) {
                            if (g < point) {
                                t.height[y][x] -= 0.01;
                                img.setRGB(x, y, transparent);
                                t.height[coordinates[1] + 1][coordinates[0]] += 0.01;
                                img.setRGB(coordinates[0], coordinates[1] + 1, blue);
                            }
                        } else if ((h < a) & (h < b) & (h < c) & (h < d) & (h < e) & (h < f) & (h < g)) {
                            if (h < point) {
                                t.height[y][x] -= 0.01;
                                img.setRGB(x, y, transparent);
                                t.height[coordinates[1] + 1][coordinates[0] + 1] += 0.01;
                                img.setRGB(coordinates[0]+1, coordinates[1] + 1, blue);
                            }
                        }
                    }
                }
            }
        }
    }
    public BufferedImage getImg(){return img;}
    public void removeWater(){
//        int transparent=(0<<24) | (0<<16) | (0<<8) | (0);
        for(int y=0;y<img.getHeight();y++){
            for(int x=0;x<img.getWidth();x++){
                int p=img.getRGB(x,y);
                int a=(p>>24)&0xff;
                p=(a<<24)|(0<<16)|(0<<8)|0;
                img.setRGB(x,y,p);
            }
        }
    }
}