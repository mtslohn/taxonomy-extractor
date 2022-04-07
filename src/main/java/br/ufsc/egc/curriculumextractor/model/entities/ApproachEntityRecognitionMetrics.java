package br.ufsc.egc.curriculumextractor.model.entities;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class ApproachEntityRecognitionMetrics {

	private int numberOfTokens;
	private int recognizedTokens;
	private int usedTokens;
	private Set<String> usedEntities;

	public ApproachEntityRecognitionMetrics(int numberOfTokens, int recognizedTokens, int usedTokens, Set<String> usedEntities) {
		this.numberOfTokens = numberOfTokens;
		this.recognizedTokens = recognizedTokens;
		this.usedTokens = usedTokens;
		this.usedEntities = usedEntities;
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
		return recognizedTokens / (numberOfTokens * 1.0);
	}

	public double getUsedTokensFactor() {
		return usedTokens / (numberOfTokens * 1.0);
	}

	public Set<String> getUsedEntities() {
		return usedEntities;
	}

	public void setUsedEntities(Set<String> usedEntities) {
		this.usedEntities = usedEntities;
	}

	public String print() {
		TreeSet<String> orderedUsedEntities = new TreeSet<String>();
		orderedUsedEntities.addAll(usedEntities);
		
		StringBuilder sbUsedEntities = new StringBuilder();
		Iterator<String> itOrderedUsedEntities = orderedUsedEntities.iterator();
		while (itOrderedUsedEntities.hasNext()) {
			sbUsedEntities.append(itOrderedUsedEntities.next());
			if (itOrderedUsedEntities.hasNext()) {
				sbUsedEntities.append(", ");
			}
		}
		
		DecimalFormat df = new DecimalFormat("0.0000000");
		return "numberOfTokens=" + numberOfTokens + "\nrecognizedTokens=" + recognizedTokens + "\nusedTokens="
				+ usedTokens + "\nrecognizementFactor=" + df.format(getRecognizementFactor()) + "\nusedTokensFactor="
				+ df.format(getUsedTokensFactor()) + "\nusedEntities=[" + sbUsedEntities + "]";
	}

}
