package FlowSkeleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;
public class Water {
    Terrain t;
    int depth;
    float water_surface;
    int x,y;

    public Water(Terrain t, int depth,int y,int x) {
        this.depth = depth;
        this.t = t;
        this.y=y;
        this.x=x;
    }
    //add   water to terrain
    public void addWater() {
        int max_row=this.y+3;
        int max_column=this.x+3;
        for(int row=this.y-3;row<max_row+1;row++){
            for(int column=this.x-3;column<max_column+1;column++){
                    this.t.height[row][column] += convertWater();
            }
        }
    }

    //draw the box on the grid point
    public void draw(Graphics g){
            g.setColor(Color.blue);
            g.fillRect(this.x,this.y,10,10);
    }

    //get the water surface at each point
    public float getWater_surface(int x, int y) {
        return (t.getHeight(x, y) + convertWater());
    }
    // water unit corresponds to a depth of 0.01m so this depth is 0.03m
    public float convertWater() {
        return (float) ((this.depth) * 0.01);
    }

    //transfer water to the lowest point among the surrounding grid points from the current point
    public void transferWater() {
        float point;
        int row = t.dimy;
        int columns = t.dimx;
        if (!((this.x - 1 == 0) | (this.x + 1 == columns - 1) | (this.y - 1 == 0) | (this.y + 1 == row - 1))) {

            //check if there is water at that point
            point = getWater_surface(this.x, this.y);
            float a, b, c, d, e, f, g, h;
            a = getWater_surface(x - 1, y - 1);
            b = getWater_surface(x, y - 1);
            c = getWater_surface(x + 1, y - 1);
            d = getWater_surface(x - 1, y);
            e = getWater_surface(x + 1, y);
            f = getWater_surface(x - 1, y + 1);
            g = getWater_surface(x, y + 1);
            h = getWater_surface(x + 1, y + 1);

            //The current water_surface at (x,y) is compared to the water surface of the neighbouring grid positions. A
            //single unit of water is transferred to the lowest of these neighbours, so long as the
            //water surface of this neighbour is strictly lower than that of the current grid position.
            //Otherwise no water is transferred out of the current grid position.
            if ((a < b) & (a < c) & (a < d) & (a < e) & (a < f) & (a < g) & (a < h)) {
                if (a < point) {
                    t.height[y - 1][x - 1] += 0.01;
                    t.height[this.y][this.x] -= 0.01;
                }
            } else if ((c < a) & (c < b) & (c < d) & (c < e) & (c < f) & (c < g) & (c < h)) {
                if (b < point) {
                    t.height[y - 1][x + 1] += 0.01;
                    t.height[this.y][this.x] -= 0.01;
                }
            } else if ((d < a) & (d < b) & (d < c) & (d < e) & (d < f) & (d < g) & (d < h)) {
                if (d < point) {
                    t.height[y][x - 1] += 0.01;
                    t.height[this.y][this.x] -= 0.01;
                }
            } else if ((e < a) & (e < b) & (e < c) & (e < d) & (e < f) & (e < g) & (e < h)) {
                if (b < point) {
                    t.height[y][x + 1] += 0.01;
                    t.height[this.y][this.x] -= 0.01;
                }
            } else if ((f < a) & (f < b) & (f < c) & (f < d) & (f < e) & (f < g) & (e < h)) {
                if (f < point) {
                    t.height[y + 1][x - 1] += 0.01;
                    t.height[this.y][this.x] -= 0.01;
                }
            } else if ((g < a) & (g < b) & (g < c) & (g < d) & (g < e) & (g < f) & (g < h)) {
                if (g < point) {
                    t.height[y + 1][x] += 0.01;
                    t.height[this.y][this.x] -= 0.01;
                }
            } else if ((h < a) & (h < b) & (h < c) & (h < d) & (h < e) & (h < f) & (h < g)) {
                if (h < point) {
                    t.height[y + 1][x + 1] += 0.01;
                    t.height[this.y][this.x] -= 0.01;
                }
            }
        }
    }


}