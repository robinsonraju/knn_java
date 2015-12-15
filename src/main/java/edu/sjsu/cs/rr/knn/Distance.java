package edu.sjsu.cs.rr.knn;

public class Distance {

	// Distance either “euclidean” or “cosine” distance,
	public enum DistanceType {
		euclidean, cosine
	};

	/**
	 * getDistance 
	 * 
	 * @param vectorA
	 * @param vectorB
	 * @param distType
	 * @return distance double
	 */
	public static double getDistance(double[] vectorA, double[] vectorB,
			DistanceType distType) {
		double distance = 0.0;

		switch (distType) {
			case euclidean:
				return computeEuclideanDistance(vectorA, vectorB);
			case cosine:
				return computeCosineDistance(vectorA, vectorB);
			default:
				break;
		}

		return distance;
	}

	/**
	 * computeEuclideanDistance
	 * 
	 * @param vectorA
	 * @param vectorB
	 * @return distance double
	 */
	public static double computeEuclideanDistance(double[] vectorA,
			double[] vectorB) {
		double sum = 0.0;

		for (int index = 0; index < vectorA.length; index++) {
			sum += Math.pow((vectorA[index] - vectorB[index]), 2.0);
		}
		return Math.sqrt(sum);
	}

	/**
	 * @return distance double
	 * 
	 * @param vectorA
	 * @param vectorB
	 * @return distance double
	 */
	public static double computeCosineDistance(double[] vectorA,
			double[] vectorB) {
		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			normA += Math.pow(vectorA[i], 2);
			normB += Math.pow(vectorB[i], 2);
		}
		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}

}
