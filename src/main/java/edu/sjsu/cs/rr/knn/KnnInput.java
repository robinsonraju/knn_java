package edu.sjsu.cs.rr.knn;

import java.io.File;
import java.io.FileNotFoundException;

import edu.sjsu.cs.rr.knn.Distance.DistanceType;

public class KnnInput {
	private final int hold_out;
	private final int k;
	private final DistanceType dist; 
	private final File inputFile;
	private final File outputFile;
	private final int featureCount;
	
	// Input type
	public enum InputType {
		HOLD_OUT("Hold-out percentage. Should be >=5 and <=20"), 
		K("Value of K. Should be greater than 0."), 
		DISTANCE("Distance Function. Should be euclidean or cosine"),
		INPUT_FILE("Input file. Should exist."),
		OUTPUT_FILE("Output file. Enter valid string. Should not exist.");

		private String inputTypeStr;

		private InputType(String c) {
			inputTypeStr = c;
		}

		public String getInputTypeStr() {
			return inputTypeStr;
		}
	}

	public KnnInput(
			int hold_out,
			int k, 
			DistanceType dist,  
			File input, 
			File output,
			int featureCount) {
		this.hold_out = hold_out;
		this.k = k;
		this.dist = dist;
		this.inputFile = input;
		this.outputFile = output;
		this.featureCount = featureCount;
	}

	public int getHold_out() {
		return hold_out;
	}

	public int getK() {
		return k;
	}

	public DistanceType getDist() {
		return dist;
	}

	public File getInputFile() {
		return inputFile;
	}

	public File getOutputFile() {
		return outputFile;
	}

	public int getFeatureCount() {
		return featureCount;
	}

	@Override
	public String toString() {
		return "KnnInput [hold_out=" + hold_out + ", k=" + k + ", dist=" + dist
				+ ", inputFile=" + inputFile + ", outputFile=" + outputFile
				+ ", featureCount=" + featureCount + "]";
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getInput(String input, InputType inputType,
			T outputType) throws Exception {
		
		try {
			switch (inputType) {
			case HOLD_OUT:
				Integer hold_out =  Integer.valueOf(input);
				if (hold_out >= 5 && hold_out <=20) {
					return (T) hold_out;
				} else {
					throw new Exception();
				}
				
			case K:
				Integer k =  Integer.valueOf(input);
				if (k > 0) {
					return (T) k;
				} else {
					throw new Exception();
				}
				
			case DISTANCE:
				return (T) DistanceType.valueOf(input);
				
			case INPUT_FILE:
				return (T) checkFile(input);
			
			case OUTPUT_FILE:
				if (notNullOrEmpty(input) && !fileExists(input)) {
					return (T) new File(input);	
				} else {
					throw new Exception();
				}

			default:
				throw new Exception("Invalid Input : " + input + " " + inputType.getInputTypeStr());	
			}
		} catch (Exception e) {
			throw new Exception("Invalid Input : " + input + " " + inputType.getInputTypeStr() , e);
		}
	}
	
	/**
	 * 
	 * @param filePathString
	 * @return File
	 * @throws Exception
	 */
	private static File checkFile(String filePathString) throws Exception {
		File f = new File(filePathString);
		if (f.exists() && !f.isDirectory()) { 
		    return f;
		} else {
			throw new FileNotFoundException();
		}
	}
	
	/**
	 * 
	 * @param str
	 * @return boolean
	 */
	private static boolean notNullOrEmpty (String str) {
		return (str != null && !str.isEmpty());
	}
	
	/**
	 * 
	 * @param filePathString
	 * @return boolean
	 */
	private static boolean fileExists(String filePathString){
		File f = new File(filePathString);
		return f.exists() && !f.isDirectory();
	}
}
