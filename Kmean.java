import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
public class Kmean {
	public class Point{
		public double xcoord;
		public double ycoord;
		public int label;
		double distance;
		Point(){
			xcoord= 0.0;
			ycoord = 0.0;
			label = 1; 
			distance = 99999.0;
		}
	}
	public int K;
	public int numPts = 0;
	Point[] pointSet; 
	public int numRows;
	public int numCols;
	int[][] imgAry;
	Point[] Kcentroids;
	public int change;

	Kmean(int rows, int cols, int pts, int numClus){
		numRows = rows;
		numCols = cols;
		numPts = pts;
		K = numClus;
		pointSet = new Point[numPts];
		for(int i = 0; i < numPts; i++)
			pointSet[i] = new Point();
		imgAry = new int[numRows][numCols];
		Kcentroids = new Point[K+1];
		for(int i = 1; i < K+1; i++) {
			Kcentroids[i] = new Point();
			Kcentroids[i].distance = 0.0;
		}
		change = 0;
	}

	public void loadPointSet(Scanner inFile, Point[] pointSet) {
		int index = 0;
		while(inFile.hasNext()) {
			int x = inFile.nextInt();
			int y = inFile.nextInt();
			pointSet[index].xcoord = (double)x;
			pointSet[index].ycoord = (double)y;
			pointSet[index].label = 0;
			pointSet[index].distance = 99999.00;
			index++;
		}
	}
	public void assignLabel(Point[] pointSet, int K) {
		int front = 0;
		int back = numPts - 1; 
		int label = 1;
		while(front <= back) {
			if(label > K)
				label = 1;
			pointSet[front].label = label;
			front++;
			label++;
			if(label > K)
				label = 1;
			pointSet[back].label = label;
			back--;
			label++;
		}
	}

	public void Point2Image(Point[] pointSet, int[][] imgAry) {
		for(int i = 0; i < pointSet.length; i++)
			this.imgAry [(int)pointSet[i].xcoord] [(int)pointSet[i].ycoord] = pointSet[i].label;
	}

	public void kMeansClustering(Point[] pointSet, int K, BufferedWriter outFile) throws IOException {
		int iteration = 0;
		this.assignLabel(pointSet, K);
		change = 3;
		while(change > 2) {
			this.Point2Image(pointSet, imgAry);
			this.prettyPrint(this.imgAry, outFile, iteration);
			this.change = 0;
			this.computeCentroids(pointSet,this.Kcentroids);
			int index = 0;
			while(index < numPts) {
				this.DistanceMinLabel(pointSet[index], this.Kcentroids);
				index++;
			}
			iteration++;
		}
	}

	public void computeCentroids(Point[] pointSet, Point[] Kcentroids) {
		double[] sumX = new double[K+1];
		for(int i = 0; i <= K; i++)
			sumX[i] = 0;
		double[] sumY = new double[K+1];
		for(int i = 0; i <= K; i++)
			sumY[i] = 0;
		int[] totalPt = new int[K+1];
		for(int i = 0; i <= K; i++)
			totalPt[i] = 0;

		int index = 0;
		while(index < numPts) {
			int label = pointSet[index].label;
			sumX[label] += pointSet[index].xcoord;
			sumY[label] += pointSet[index].ycoord;
			totalPt[label]++;
			index++;
		}
		int label = 1;
		while(label <= K) {
			Kcentroids[label].xcoord = (sumX[label] / totalPt[label]);
			Kcentroids[label].ycoord = (sumY[label] / totalPt[label]);
			label++;
		}
	}

	public void DistanceMinLabel(Point pt, Point[] Kcentroids) {
		double minDist = 99999.00;
		int minLabel = 0;
		int label = 1;
		while(label <= K) {
			double dist = computeDist(pt, Kcentroids[label]);
			if(dist < minDist) {
				minLabel = label;
				minDist = dist;
			}
			label++;
		}
		pt.distance = minDist;
		if (pt.label != minLabel) {
			pt.label = minLabel;
			this.change++;
		}
	}

	public double computeDist(Point pt, Point cenPt) {
		double xdist = Math.pow((pt.xcoord - cenPt.xcoord) , 2);
		double ydist = Math.pow((pt.ycoord - cenPt.ycoord) , 2);
		return Math.sqrt((xdist) + (ydist));
	}

	public void prettyPrint(int[][] imgAry, BufferedWriter outFile, int iteration) throws IOException {
		outFile.write("*** Result of iteration " + iteration + "***:\n");
		for(int i = 0; i < this.numRows; i++) {
			for(int j = 0; j < this.numCols; j++) {
				if(this.imgAry[i][j] > 0)
					outFile.write(this.imgAry[i][j] + "");
				else
					outFile.write(" ");
			}
			outFile.write("\n");
		}
	}

}