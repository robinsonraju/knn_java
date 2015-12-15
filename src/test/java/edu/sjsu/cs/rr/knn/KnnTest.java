package edu.sjsu.cs.rr.knn;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import cs286.Knn;

/**
 * Unit test for simple App.
 */
public class KnnTest extends TestCase {
	Knn knn = new Knn();
	
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public KnnTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(KnnTest.class);
	}

	/**
	 * Test valid input data
	 */
	public void testValidInputData() {
		String[] input = {"10", "100", "euclidean", "src/main/resources/iris-data.txt", "src/main/resources/output1.txt"};
		
		try {
			knn.validateInputData(input);
			assertTrue(true);
		} catch (Exception e) {
			fail("Input validation failed");
		}
	}
}

