package edu.sjsu.cs.rr.knn;

import java.util.Arrays;
import java.util.StringTokenizer;

public class Record implements Comparable<Record>{

	final double[] features;
	final int clazz;
	double distance;
	
	public Record(double[] features, int cls) {
		this.features = features;
		this.clazz = cls;
		distance = 0.0;
	}
	
	public Record(String rec) {
		StringTokenizer st = new StringTokenizer(rec);
		int len = st.countTokens();
		features = new double[len-1];
		for (int j = 0; j < (len-1); j++) {
			features[j] = Double.parseDouble(st.nextToken());
		}
		clazz = (int)Double.parseDouble(st.nextToken());
	}

	
	public double[] getFeatures() {
		return features;
	}

	public int getClazz() {
		return clazz;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(Record r) {
		return (new Double(this.distance)).compareTo(r.distance);
	}
	

	@Override
	public String toString() {
		return "Record [features=" + Arrays.toString(features) + ", clazz="
				+ clazz + ", distance=" + distance + "]";
	}
}
