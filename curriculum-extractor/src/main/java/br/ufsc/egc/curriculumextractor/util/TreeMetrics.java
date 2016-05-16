package br.ufsc.egc.curriculumextractor.util;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;

public class TreeMetrics {
	
	private int nodeNumber = 0;
	private int maxLevel = 1; // root
	private double expansionLevelAvg;
	private double densityAvg;
	
	public TreeMetrics(Tree tree) {
		calculate(tree);
	}

	private void calculate(Tree tree) {
		for (Term term: tree.getRoots()) {
			navegateAndCalculate(term, 1);
		}
	}

	private void navegateAndCalculate(Term term, int level) {
		
	}}
