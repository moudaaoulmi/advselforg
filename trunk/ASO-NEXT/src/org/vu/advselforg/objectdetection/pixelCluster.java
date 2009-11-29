package org.vu.advselforg.objectdetection;

import java.util.Iterator;
import java.util.ArrayList;

/**
 *
 * @author ivos2
 */
public class pixelCluster implements Iterable<coordinate> {
    ArrayList<coordinate> coordinates; //The coordinates

    pixelCluster(){
    //Creates the empty pixel cluster
        coordinates = new ArrayList<coordinate>();
    }
    
    pixelCluster(int x, int y){
    //Creates a pixel cluster with one pixel
        coordinates = new ArrayList<coordinate>();
        coordinates.add(new coordinate(x, y));
    }

    pixelCluster(coordinate coordinate){
    //Creates a pixel cluster with one pixel as a coordinate
        coordinates = new ArrayList<coordinate>();
        coordinates.add(coordinate);
    }
    
    void addPixel(int x, int y){
    //Add a pixel described by an x and y value
        coordinates.add(new coordinate(x, y));
    }

    void addCoordinate(coordinate coordinate){
    //Add a pixel described by a coordinate
        coordinates.add(coordinate);
    }

    public Iterator<coordinate> iterator() {
        return coordinates.iterator();
    }

    public boolean partOf(pixelCluster cluster, double distanceThreshold){
    //Compares two pixelcluster to see if they should become one
    //If even one pixel of either cluster is within threshold distance of a pixel of the other cluster
    //Then this function returns true
        for (coordinate otherCoordinate : cluster){
            for (coordinate thisCoordinate : this){
                if (thisCoordinate.distance(otherCoordinate) < distanceThreshold) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean partOf(coordinate otherCoordinate, double distanceThreshold){
    //Compares a coordinate with this pixelcluster to see if the coordinate should become part of this cluster
    //If even one pixel of the cluster is within threshold distance of the coordinate
    //Then this function returns true
       for (coordinate thisCoordinate : this){
            if (thisCoordinate.distance(otherCoordinate) < distanceThreshold) {
                return true;
            }
        }
        return false;
    }

    public void merge(pixelCluster cluster){
    //Adds all coordinates of cluster to this cluster
         for (coordinate otherCoordinate : cluster){
             this.addCoordinate(otherCoordinate);
         }
    }


    public coordinate center(){
    //Gives the center of the cluster by using the mean of the most extreme X and Y values.
        int maxX = Integer.MIN_VALUE,
            minX = Integer.MAX_VALUE,
            maxY = Integer.MIN_VALUE,
            minY = Integer.MAX_VALUE;

        int x, y;

        double centerX, centerY;

        for(coordinate currentCoordinate : coordinates){
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

        return new coordinate((int)centerX,(int)centerY);
    }

    int size(){
    //Returns the number of coordinates within this pixelCluster
        return coordinates.size();
    }
}
