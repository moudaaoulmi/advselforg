import java.awt.image.*;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;

public class Main {
//The picture and its sizes. It should be possible to get the sizes out of the picture itself so this could be improved.
    static Image image = new ImageIcon("test_images/test.jpg").getImage();
    static int width = 50;
    static int height = 50;

//The three thresholds that decide how cluster are found.
    final static int redThreshold = 150;        //The minimum red value for a pixel to be considerd.
    final static double distanceTreshold = 2;   //The maximum distance between pixels within a single cluster
    final static int sizeThreshold = 3;         //The minimum size for a cluster to be considerd an object

//The global variable containing the clusters.
    static ArrayList<pixelCluster> pixelClusters;

    public static void main(String[] args) {
        pixelClusters = new ArrayList<pixelCluster>();    //initialize the pixelClusters
        int[] pixels = getPixels();         //Get the pixelvalues of the picture

        //System.out.println(pixels.length);
        
        //Process all pixels of the image
        for(int y=0; y<height; y++){
            for(int x=0; x<width; x++){
                if(getRed(x, y, pixels)> redThreshold){
                    processCoordinate(new coordinate(x,y));
                }
            }
        }

        //Remove small clusters
        Iterator<pixelCluster> iterator = pixelClusters.iterator();

        while (iterator.hasNext()){
            pixelCluster cluster = (pixelCluster)iterator.next();
            
            if(cluster.size() < sizeThreshold){
                iterator.remove();
            }
        }

        //Debug print
        System.out.println(pixelClusters.size());

        for(pixelCluster cluster : pixelClusters){
            coordinate coordinate = cluster.center();
            System.out.println("Center: X=" + coordinate.getX() + " Y=" + coordinate.getY());
        }
    }

    static int getRed(int x, int y, int[] pixels){
    //Gives the red RGB value of the pixel. Can be changed to something else if disired.
        return (pixels[y*width + x] >> 16) & 0xff;
    }

    static int[] getPixels(){
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

    static void processCoordinate(coordinate coordinate){
    //Adds a coordinate to a pixel cluster if the coordinate is close enough
    //Else the coordinate becomes a new pixel cluster
        ArrayList<pixelCluster> temp = new ArrayList<pixelCluster>();

        if (pixelClusters.isEmpty()){
            //If there are no other pixelCluster yet this one becomes the first
            pixelClusters.add(new pixelCluster(coordinate));
        } else {
            //Check to how which pixelclusters this coordinate belongs and store them in temp
            for(pixelCluster cluster : pixelClusters){
                if (cluster.partOf(coordinate, distanceTreshold)){
                    temp.add(cluster);
                }
            }

            if (temp.isEmpty()){
            //If there was no cluster to which this coordinate should belong it becomes its own cluster
                pixelClusters.add(new pixelCluster(coordinate));
            } else {
            //If there where cluster that should contain coordinate merge all these cluster into a single cluster
            //And add this coordinate to this new cluster
                pixelCluster newCluster = new pixelCluster();
                for(pixelCluster cluster : temp){
                    newCluster.merge(cluster);
                    pixelClusters.remove(cluster);
                }
                newCluster.addCoordinate(coordinate);
                
                pixelClusters.add(newCluster);
            }

        }
    }


}
