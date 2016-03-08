package br.ufsc.egc.curriculumextractor.model;

import java.util.ArrayList;
import java.util.List;

public class CurriculumCorrelation {

	private int curriculumId;
	private List<EntityPair> pairs = new ArrayList<EntityPair>();

	public int getCurriculumId() {
		return curriculumId;
	}

	public void setCurriculumId(int curriculumId) {
		this.curriculumId = curriculumId;
	}

	public List<EntityPair> getPairs() {
		return pairs;
	}

	public void setPairs(List<EntityPair> pairs) {
		this.pairs = pairs;
	}

}
