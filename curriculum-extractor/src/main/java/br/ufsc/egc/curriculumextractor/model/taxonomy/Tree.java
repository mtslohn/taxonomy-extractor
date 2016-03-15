package br.ufsc.egc.curriculumextractor.model.taxonomy;

import java.util.ArrayList;
import java.util.List;

public class Tree {
	
	private List<Term> roots;
	
	public Tree() {
		roots = new ArrayList<Term>();
	}

	public List<Term> getRoots() {
		return roots;
	}

	public void setRoots(List<Term> roots) {
		this.roots = roots;
	}
	
	public Term find(String label) {
		return find(roots, label);
	}

	private Term find(List<Term> terms, String label) {
		for (Term term: terms) {
			if (term.getLabel().equals(label)) {
				return term;
			} else {
				Term result = find(term.getSons(), label);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
	public void addRoot(Term term) {
		roots.add(term);
	}
	
}
