import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
public class Project8_main{
	public static void main (String[] args) throws IOException {
		//Step 0
		Scanner inFile = new Scanner(new FileReader(args[0]));
		BufferedWriter outFile = new BufferedWriter(new FileWriter(args[1]));
		int rows = inFile.nextInt();
		int cols = inFile.nextInt();
		int pts = inFile.nextInt();
		System.out.println("Enter a number, K = ");
		Scanner in = new Scanner(System.in);
		int numClus = in.nextInt();
		Kmean KMean = new Kmean(rows, cols, pts, numClus);
		KMean.loadPointSet(inFile, KMean.pointSet);
		KMean.kMeansClustering(KMean.pointSet, KMean.K, outFile);
		inFile.close();
		outFile.close();
		in.close();
	}
}