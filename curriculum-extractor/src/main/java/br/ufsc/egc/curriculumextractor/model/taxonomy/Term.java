package br.ufsc.egc.curriculumextractor.model.taxonomy;

import java.util.ArrayList;
import java.util.List;

public class Term {

	private String label;
	private List<Term> sons;
	
	public Term() {
		label = "";
		sons = new ArrayList<Term>();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Term> getSons() {
		return sons;
	}

	public void setSons(List<Term> sons) {
		this.sons = sons;
	}
	
	public void addSon(Term term) {
		sons.add(term);
	}

}
