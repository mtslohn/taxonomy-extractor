package br.ufsc.egc.curriculumextractor.util;

public class NERMetrics {

	private int numberOfTokens;
	private int recognizedTokens;
	private int usedTokens;

	public NERMetrics(int numberOfTokens, int recognizedTokens, int usedTokens) {
		this.numberOfTokens = numberOfTokens;
		this.recognizedTokens = recognizedTokens;
		this.usedTokens = usedTokens;
	}

	public int getNumberOfTokens() {
		return numberOfTokens;
	}

	public void setNumberOfTokens(int numberOfTokens) {
		this.numberOfTokens = numberOfTokens;
	}

	public int getRecognizedTokens() {
		return recognizedTokens;
	}

	public void setRecognizedTokens(int recognizedTokens) {
		this.recognizedTokens = recognizedTokens;
	}

	public int getUsedTokens() {
		return usedTokens;
	}

	public void setUsedTokens(int usedTokens) {
		this.usedTokens = usedTokens;
	}
	
	public double getRecognizementFactor() {
		return recognizedTokens/(numberOfTokens * 1.0);
	}
	
	public double getUsedTokensFactor() {
		return usedTokens/(numberOfTokens * 1.0);
	}

	public String print() {
		return "numberOfTokens=" + numberOfTokens + "\nrecognizedTokens=" + recognizedTokens
				+ "\nusedTokens=" + usedTokens + "\nrecognizementFactor=" + getRecognizementFactor() + "\nusedTokensFactor=" + getUsedTokensFactor();
	}
	
}
