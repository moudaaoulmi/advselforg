import java.util.ArrayList;
import java.util.Iterator;

class PixelCluster implements Iterable {
	private ArrayList coordinates; // The coordinates

	PixelCluster() {
		// Creates the empty pixel cluster
		coordinates = new ArrayList();
	}

	PixelCluster(int x, int y) {
		// Creates a pixel cluster with one pixel
		coordinates = new ArrayList();
		coordinates.add(new Coordinate(x, y));
	}

	PixelCluster(Coordinate coordinate) {
		// Creates a pixel cluster with one pixel as a coordinate
		coordinates = new ArrayList();
		coordinates.add(coordinate);
	}

	void addPixel(int x, int y) {
		// Add a pixel described by an x and y value
		coordinates.add(new Coordinate(x, y));
	}

	void addCoordinate(Coordinate coordinate) {
		// Add a pixel described by a coordinate
		coordinates.add(coordinate);
	}

	public Iterator iterator() {
		return coordinates.iterator();
	}

	public boolean partOf(PixelCluster cluster, double distanceThreshold) {
		// Compares two pixelcluster to see if they should become one
		// If even one pixel of either cluster is within threshold distance
		// of a pixel of the other cluster
		// Then this function returns true
		for (int i = 0; i < cluster.size(); i++) {
			Coordinate otherCoordinate = cluster.get(i);
			for (int j = 0; j < this.size(); j++) {
				Coordinate thisCoordinate = this.get(j);
				if (thisCoordinate.distance(otherCoordinate) < distanceThreshold) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean partOf(Coordinate otherCoordinate, double distanceThreshold) {
		// Compares a coordinate with this pixelcluster to see if the
		// coordinate should become part of this cluster
		// If even one pixel of the cluster is within threshold distance of
		// the coordinate
		// Then this function returns true
		for (int i = 0; i < this.size(); i++) {
			Coordinate thisCoordinate = this.get(i);
			if (thisCoordinate.distance(otherCoordinate) < distanceThreshold) {
				return true;
			}
		}
		return false;
	}

	public void merge(PixelCluster cluster) {
		// Adds all coordinates of cluster to this cluster
		for (int i = 0; i < cluster.size(); i++) {
			Coordinate otherCoordinate = cluster.get(i);
			this.addCoordinate(otherCoordinate);
		}
	}

	public Coordinate center() {
		// Gives the center of the cluster by using the mean of the most
		// extreme X and Y values.
		int maxX = Integer.MIN_VALUE, minX = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE, minY = Integer.MAX_VALUE;

		int x, y;

		double centerX, centerY;

		for (int i = 0; i < this.size(); i++) {
			Coordinate currentCoordinate = this.get(i);
			x = currentCoordinate.getX();
			y = currentCoordinate.getY();

			if (x > maxX) {
				maxX = x;
			}

			if (y > maxY) {
				maxY = y;
			}

			if (x < minX) {
				minX = x;
			}

			if (y < minY) {
				minY = y;
			}
		}

		centerX = (maxX + minX) / 2;
		centerY = (maxY + minY) / 2;

		return new Coordinate((int) centerX, (int) centerY);
	}

        public double clusterSizeRatio() {
                int maxX = Integer.MIN_VALUE, minX = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE, minY = Integer.MAX_VALUE;

	        int x, y;

		double difX, difY;

		for (int i = 0; i < this.size(); i++) {
			Coordinate currentCoordinate = this.get(i);
			x = currentCoordinate.getX();
			y = currentCoordinate.getY();

			if (x > maxX) {
				maxX = x;
			}

			if (y > maxY) {
				maxY = y;
			}

			if (x < minX) {
				minX = x;
			}

			if (y < minY) {
				minY = y;
			}
		}
                
                difX = Math.abs(maxX - minX);
                difY = Math.abs(maxY - minY);
                
                return max(difX/difY, difY/difX);
        }
        
        double max(double a, double b) {
          return a > b ? a : b; 
        }

	int size() {
		// Returns the number of coordinates within this pixelCluster
		return coordinates.size();
	}

        Coordinate get(int i) {
          return (Coordinate) coordinates.get(i);
        }
}

