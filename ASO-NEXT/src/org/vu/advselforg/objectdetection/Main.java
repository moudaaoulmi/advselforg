package org.vu.advselforg.objectdetection;

import java.awt.image.*;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;

public class Main {
	//The picture and its sizes. It should be possible to get the sizes out of the picture itself so this could be improved.
	protected Image image = new ImageIcon("test_images/test.jpg").getImage();
	protected int width = 50;
	protected int height = 50;

    //The three thresholds that decide how cluster are found.
    final static int redThreshold = 150;        //The minimum red value for a pixel to be considerd.
    final static double distanceTreshold = 2;   //The maximum distance between pixels within a single cluster
    final static int sizeThreshold = 3;         //The minimum size for a cluster to be considerd an object

    //The global variable containing the clusters.
    protected ArrayList<PixelCluster> pixelClusters;

    protected void run() {
        pixelClusters = new ArrayList<PixelCluster>();    //initialize the pixelClusters
        int[] pixels = getPixels();         //Get the pixelvalues of the picture

        //System.out.println(pixels.length);
        
        //Process all pixels of the image
        for(int y=0; y<height; y++){
            for(int x=0; x<width; x++){
                if(getRed(x, y, pixels)> redThreshold){
                    processCoordinate(new Coordinate(x,y));
                }
            }
        }

        //Remove small clusters
        Iterator<PixelCluster> iterator = pixelClusters.iterator();

        while (iterator.hasNext()){
            PixelCluster cluster = (PixelCluster)iterator.next();
            
            if(cluster.size() < sizeThreshold){
                iterator.remove();
            }
        }

        //Debug print
        System.out.println(pixelClusters.size());

        for(PixelCluster cluster : pixelClusters){
            Coordinate coordinate = cluster.center();
            System.out.println("Center: X=" + coordinate.getX() + " Y=" + coordinate.getY());
        }
    }

    protected int getRed(int x, int y, int[] pixels){
    //Gives the red RGB value of the pixel. Can be changed to something else if disired.
        return (pixels[y*width + x] >> 16) & 0xff;
    }

    protected int[] getPixels(){
    //Read all pixels of the image
        //Construct
        PixelGrabber pixelGrabber=new PixelGrabber(image, 0,0, width, height, true);

        //Grab
        try
        {
            pixelGrabber.grabPixels();
        }
        catch (Exception e)
        {
            System.out.println("PixelGrabber exception");
        }
        
        //Return the pixel values
        return (int[])pixelGrabber.getPixels();
    }

    protected void processCoordinate(Coordinate coordinate){
    //Adds a coordinate to a pixel cluster if the coordinate is close enough
    //Else the coordinate becomes a new pixel cluster
        ArrayList<PixelCluster> temp = new ArrayList<PixelCluster>();

        if (pixelClusters.isEmpty()){
            //If there are no other pixelCluster yet this one becomes the first
            pixelClusters.add(new PixelCluster(coordinate));
        } else {
            //Check to how which pixelclusters this coordinate belongs and store them in temp
            for(PixelCluster cluster : pixelClusters){
                if (cluster.partOf(coordinate, distanceTreshold)){
                    temp.add(cluster);
                }
            }

            if (temp.isEmpty()){
            //If there was no cluster to which this coordinate should belong it becomes its own cluster
                pixelClusters.add(new PixelCluster(coordinate));
            } else {
            //If there where cluster that should contain coordinate merge all these cluster into a single cluster
            //And add this coordinate to this new cluster
                PixelCluster newCluster = new PixelCluster();
                for(PixelCluster cluster : temp){
                    newCluster.merge(cluster);
                    pixelClusters.remove(cluster);
                }
                newCluster.addCoordinate(coordinate);
                
                pixelClusters.add(newCluster);
            }

        }
    }
    
	public static void main() {
		Main prog = new Main();
		prog.run();
	}

    protected class Coordinate {
        int x, y;

        public Coordinate(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public double distance(Coordinate other){
            int otherX, otherY;
            int a, b;
            otherX = other.getX();
            otherY = other.getY();

            a = Math.abs(x - otherX);
            b = Math.abs(y - otherY);
            return Math.sqrt(a*a + b*b);
        }
    }
    
	protected class PixelCluster implements Iterable<Coordinate> {
        ArrayList<Coordinate> coordinates; //The coordinates

        PixelCluster(){
        //Creates the empty pixel cluster
            coordinates = new ArrayList<Coordinate>();
        }
        
        PixelCluster(int x, int y){
        //Creates a pixel cluster with one pixel
            coordinates = new ArrayList<Coordinate>();
            coordinates.add(new Coordinate(x, y));
        }

        PixelCluster(Coordinate coordinate){
        //Creates a pixel cluster with one pixel as a coordinate
            coordinates = new ArrayList<Coordinate>();
            coordinates.add(coordinate);
        }
        
        void addPixel(int x, int y){
        //Add a pixel described by an x and y value
            coordinates.add(new Coordinate(x, y));
        }

        void addCoordinate(Coordinate coordinate){
        //Add a pixel described by a coordinate
            coordinates.add(coordinate);
        }

        public Iterator<Coordinate> iterator() {
            return coordinates.iterator();
        }

        public boolean partOf(PixelCluster cluster, double distanceThreshold){
        //Compares two pixelcluster to see if they should become one
        //If even one pixel of either cluster is within threshold distance of a pixel of the other cluster
        //Then this function returns true
            for (Coordinate otherCoordinate : cluster){
                for (Coordinate thisCoordinate : this){
                    if (thisCoordinate.distance(otherCoordinate) < distanceThreshold) {
                        return true;
                    }
                }
            }

            return false;
        }

        public boolean partOf(Coordinate otherCoordinate, double distanceThreshold){
        //Compares a coordinate with this pixelcluster to see if the coordinate should become part of this cluster
        //If even one pixel of the cluster is within threshold distance of the coordinate
        //Then this function returns true
           for (Coordinate thisCoordinate : this){
                if (thisCoordinate.distance(otherCoordinate) < distanceThreshold) {
                    return true;
                }
            }
            return false;
        }

        public void merge(PixelCluster cluster){
        //Adds all coordinates of cluster to this cluster
             for (Coordinate otherCoordinate : cluster){
                 this.addCoordinate(otherCoordinate);
             }
        }


        public Coordinate center(){
        //Gives the center of the cluster by using the mean of the most extreme X and Y values.
            int maxX = Integer.MIN_VALUE,
                minX = Integer.MAX_VALUE,
                maxY = Integer.MIN_VALUE,
                minY = Integer.MAX_VALUE;

            int x, y;

            double centerX, centerY;

            for(Coordinate currentCoordinate : coordinates){
                x = currentCoordinate.getX();
                y = currentCoordinate.getY();

                if(x > maxX){
                    maxX = x;
                }

                if(y > maxY){
                    maxY = y;
                }

                if(x < minX){
                    minX = x;
                }

                if(y < minY){
                    minY = y;
                }
            }

            centerX = (maxX + minX)/2;
            centerY = (maxY + minY)/2;

            return new Coordinate((int)centerX,(int)centerY);
        }

        int size(){
        //Returns the number of coordinates within this pixelCluster
            return coordinates.size();
        }
    }
}
