package br.ufsc.egc.curriculumextractor.model.entities;

import gnu.trove.map.TObjectIntMap;

public class EntityRecognitionData {

	private TObjectIntMap<String> entityCountMap;
	private int numberOfTokens;
	private int recognizedTokens;

	public EntityRecognitionData(TObjectIntMap<String> entityCountMap, int numberOfTokens, int recognizedTokens) {
		this.entityCountMap = entityCountMap;
		this.numberOfTokens = numberOfTokens;
		this.recognizedTokens = recognizedTokens;
	}

	public TObjectIntMap<String> getEntityCountMap() {
		return entityCountMap;
	}

	public int getNumberOfTokens() {
		return numberOfTokens;
	}

	public int getRecognizedTokens() {
		return recognizedTokens;
	}
}
