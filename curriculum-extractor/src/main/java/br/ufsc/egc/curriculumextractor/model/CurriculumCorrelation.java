package br.ufsc.egc.curriculumextractor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mapdb.Serializer;

public class CurriculumCorrelation implements Serializable {

	private static final long serialVersionUID = 1L;
	
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

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("CurriculumCorrelation [curriculumId=" + curriculumId
				+ ", pairs="); 
		Iterator<EntityPair> itPairs = pairs.iterator();
		while (itPairs.hasNext()) {
			EntityPair pair = itPairs.next();
			output.append(pair);
			if (itPairs.hasNext()) {
				output.append(", ");
			}
		}
		output.append("]");
		return output.toString();
	}

}
