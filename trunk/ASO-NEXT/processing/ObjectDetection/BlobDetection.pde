import java.util.ArrayList;
import java.util.Iterator;

public class BlobDetection {
	int width;
	int height;

	// The three parameters that decide how clusters are found
	double distanceThreshold; // The maximum distance between pixels within a single cluster
	int sizeThreshold; // The minimum size for a cluster to be considered an object
        double maxPixelClusterSizeRatio;

	// The global variable containing the clusters.
	ArrayList pixelClusters;

        BlobDetection(int width, int height, double distanceThreshold, int sizeThreshold, double maxPixelClusterSizeRatio) {
          pixelClusters = new ArrayList();
          this.width = width;
          this.height = height;
          this.distanceThreshold = distanceThreshold;
          this.sizeThreshold = sizeThreshold;
          this.maxPixelClusterSizeRatio = maxPixelClusterSizeRatio;
        }

	ArrayList process(boolean[] bools) {
                pixelClusters.clear();
  
		// Process all pixels of the image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (getBool(x, y, bools)) {
					processCoordinate(new Coordinate(x, y));
				}
			}
		}

		//Remove small clusters and cluster with a wrong size ratio
		Iterator iterator = pixelClusters.iterator();

		while (iterator.hasNext()) {
			PixelCluster cluster = (PixelCluster) iterator.next();

			if (cluster.size() < sizeThreshold || cluster.clusterSizeRatio() > maxPixelClusterSizeRatio) {
				iterator.remove();
			}
		}

                return pixelClusters;
	}

	boolean getBool(int x, int y, boolean[] bools) {
		return bools[y * width + x];
	}

	void processCoordinate(Coordinate coordinate) {
		// Adds a coordinate to a pixel cluster if the coordinate is close
		// enough
		// Else the coordinate becomes a new pixel cluster
		ArrayList temp = new ArrayList();

		if (pixelClusters.isEmpty()) {
			// If there are no other pixelCluster yet this one becomes the first
			pixelClusters.add(new PixelCluster(coordinate));
		} else {
			// Check to how which pixelclusters this coordinate belongs and
			// store them in temp
			for (int i = 0; i < pixelClusters.size(); i++) {
				PixelCluster cluster = (PixelCluster) pixelClusters.get(i);
				if (cluster.partOf(coordinate, distanceThreshold)) {
					temp.add(pixelClusters.get(i));
				}
			}

			if (temp.isEmpty()) {
				// If there was no cluster to which this coordinate should
				// belong it becomes its own cluster
				pixelClusters.add(new PixelCluster(coordinate));
			} else {
				// If there where cluster that should contain coordinate merge
				// all these cluster into a single cluster
				// And add this coordinate to this new cluster
				PixelCluster newCluster = new PixelCluster();
				for (int i = 0; i < temp.size(); i++) {
					PixelCluster cluster = (PixelCluster) temp.get(i);
					newCluster.merge(cluster);
					pixelClusters.remove(cluster);
				}
				newCluster.addCoordinate(coordinate);

				pixelClusters.add(newCluster);
			}

		}
	}
}

