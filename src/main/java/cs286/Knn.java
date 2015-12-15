package cs286;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sjsu.cs.rr.knn.Distance;
import edu.sjsu.cs.rr.knn.Distance.DistanceType;
import edu.sjsu.cs.rr.knn.KnnInput;
import edu.sjsu.cs.rr.knn.KnnInput.InputType;
import edu.sjsu.cs.rr.knn.Record;


/**
 * 
 * Knn
 */
public class Knn {
	
	/**
	 * java -cp knn.jar cs286.Knn <hold-out %> <k> <distance> <input> <output>
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Knn knn = new Knn();
		KnnInput input = knn.validateInputData(args);
		knn.doKnnClassification(input);
	}
	
	/**
	 * 
	 * @param args
	 * @return KnnInput
	 * @throws Exception
	 */
	public KnnInput validateInputData (String[] args) throws Exception {
		System.out.println("**** Reading Input parameters ****");

		int holdout_perc = KnnInput.getInput(args[0], InputType.HOLD_OUT, new Integer(0));
		
		int k = KnnInput.getInput(args[1], InputType.K, new Integer(0));

		DistanceType dist = (DistanceType) KnnInput.getInput(args[2], InputType.DISTANCE, new Object());
		
		File inputFile = (File) KnnInput.getInput(args[3], InputType.INPUT_FILE,new Object());
		
		File outputFile = (File) KnnInput.getInput(args[4], InputType.OUTPUT_FILE,new Object());
		
		// int featureCount = KnnInput.getInput(args[5], InputType.FEATURE_COUNT, new Integer(0));
		int featureCount = 4; 
		
		System.out.println("Input is valid.");
		return new KnnInput(holdout_perc, k, dist, inputFile, outputFile, featureCount);
	}
	
	public void doKnnClassification(KnnInput input) throws Exception {
		//1. Read input data from file
		List<Record> inputRecords = new ArrayList<Record>();
		try (BufferedReader br = new BufferedReader(new FileReader(input.getInputFile().getAbsolutePath()))) {
			String line;
			while ((line = br.readLine()) != null) {
				inputRecords.add(new Record(line));
			}
		}
		
		//2. Divide data into testing and training
		int holdout_perc = input.getHold_out();
		List<Record> testData = getSublist(inputRecords, holdout_perc, true);
		List<Record> trngData = getSublist(inputRecords, holdout_perc, false);
		
		List<Record> classifiedData = new ArrayList<Record>(); 
		
		//3. Iterate through test data to predict class
		for (Record testRec : testData) {
			// a. Update distances of all the trng records from this record
			for (Record trngRec : trngData) {
				//System.out.println("dist " + Distance.getDistance(testRec.getFeatures(), trngRec.getFeatures(), input.getDist()));
				trngRec.setDistance(Distance.getDistance(testRec.getFeatures(), trngRec.getFeatures(), input.getDist()));
			}
			
			// b. Sort the records by distance
			Collections.sort(trngData);
			
			// c. Pick top k
			List<Record> topKrecs =  trngData.subList(0, input.getK());
			
			// d. Choose class from the top k
			Map<Integer, Integer> classCount = getClassCountMap(topKrecs);
			
			int finalClass = 0; double finalValue = 0.0;
			for (Integer key : classCount.keySet()) {
				if (classCount.get(key) > finalValue) {
					finalValue = classCount.get(key);
					finalClass = key;
				}
			}
			
			classifiedData.add(new Record(testRec.getFeatures(), finalClass));
		}
		
        System.out.println("**** Printing output to file" + input.getOutputFile().getAbsolutePath() + "****");
        List<String> outputLines = new ArrayList<String>();
        outputLines.add("k = " + input.getK());
        outputLines.add("distance = " + input.getDist().name());
        
		Map<Integer, Integer> classCountOrig = getClassCountMap(testData);
		Map<Integer, Integer> classCountClassification = getClassCountMap(classifiedData);  
		for (Integer key : classCountOrig.keySet()) {
			outputLines.add("accuracy for species " + (key+1) + " = " +getAccuracy(classCountOrig.get(key), classCountClassification.get(key)));
		}
        Path file = Paths.get(input.getOutputFile().getAbsolutePath());
        Files.write(file, outputLines, Charset.forName("UTF-8"));
		
	}
	
	private double getAccuracy (Integer a, Integer b){
		b = (b==null)?0:b;
		b = (b>a)?a:b;
		return ((float)b/(float)a) * 100;
	}
	
	/**
	 * Get Map of class and count of occurances
	 * 
	 * @param recs
	 * @return Map<class, count>
	 */
	public Map<Integer, Integer> getClassCountMap (List<Record> recs){
		Map<Integer, Integer> classCountMap = new HashMap<Integer, Integer>();
		for (Record rec : recs) {
			int cls = rec.getClazz();
			if (classCountMap.containsKey(cls)) {
				classCountMap.put(cls, classCountMap.get(cls) + 1);
			} else {
				classCountMap.put(cls, 1);
			}
		}
		return classCountMap;
	}
	
	/**
	 * Return a sublist based on the input percentage
	 * Training data is expected to be the top x% and testing (100-x) %
	 * @param <T>
	 * @param inputData
	 * @param percTraining
	 * @param training
	 * @return
	 */
	private static <T> List<T> getSublist(List<T> inputData, int percentage, boolean testing) {
		int index = inputData.size() * percentage / 100; 
		
		if (testing) {
			return inputData.subList(0, index);
		} else {
			return inputData.subList(index, inputData.size());
		}
	}
}
