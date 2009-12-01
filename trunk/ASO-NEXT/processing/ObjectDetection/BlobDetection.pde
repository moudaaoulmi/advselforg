import java.util.ArrayList;
import java.util.Iterator;

public class BlobDetection {
	// The picture and its sizes. It should be possible to get the sizes out of
	// the picture itself so this could be improved.
	int width = 640;
	int height = 480;

	// The three thresholds that decide how cluster are found.
	final static double distanceTreshold = 2; // The maximum distance between
	// pixels within a single
	// cluster
	final static int sizeThreshold = 3; // The minimum size for a cluster to be
	// considerd an object

	// The global variable containing the clusters.
	ArrayList pixelClusters;

        BlobDetection() {
          pixelClusters = new ArrayList();
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

		// Remove small clusters
		Iterator iterator = pixelClusters.iterator();

		while (iterator.hasNext()) {
			PixelCluster cluster = (PixelCluster) iterator.next();

			if (cluster.size() < sizeThreshold) {
				iterator.remove();
			}
		}

		// Debug print
		System.out.println(pixelClusters.size());

		for (int i = 0; i < pixelClusters.size(); i++) {
			PixelCluster cluster = (PixelCluster) pixelClusters.get(i);
			Coordinate coordinate = cluster.center();
			System.out.println("Center: X=" + coordinate.getX() + " Y=" + coordinate.getY());
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
				if (cluster.partOf(coordinate, distanceTreshold)) {
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
				for (int i = 0; i < pixelClusters.size(); i++) {
					PixelCluster cluster = (PixelCluster) pixelClusters.get(i);
					newCluster.merge(cluster);
					pixelClusters.remove(pixelClusters.get(i));
				}
				newCluster.addCoordinate(coordinate);

				pixelClusters.add(newCluster);
			}

		}
	}
}

