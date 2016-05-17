package br.ufsc.egc.curriculumextractor.util;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;

public class TreeMetrics {

	private int nodeCount = 0;
	private int maxLevel = 0;
	private int expansions = 0;
	private int expansionSum = 0;
	private double expansionFactorAvg;
	private double densityAvg;
	private double termLevelAvg;

	public TreeMetrics(Tree tree) {
		calculateSums(tree);
		calculateStatistics();
	}

	private void calculateStatistics() {
		expansionFactorAvg = expansionSum/(double)expansions;
	}

	private void calculateSums(Tree tree) {
		for (Term term : tree.getRoots()) {
			calculate(term, 1);
		}
	}

	private void calculate(Term term, int level) {
		nodeCount++;
		if (level > maxLevel) {
			maxLevel = level;
		}
		if (!term.getSons().isEmpty()) {
			expansions++;
			expansionSum += term.getSons().size();
		}
		for (Term son : term.getSons()) {
			calculate(son, level + 1);
		}
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public int getExpansions() {
		return expansions;
	}

	public int getExpansionSum() {
		return expansionSum;
	}

	public double getExpansionFactorAvg() {
		return expansionFactorAvg;
	}

	public double getDensityAvg() {
		return densityAvg;
	}
	
	public double getTermLevelAvg() {
		return termLevelAvg;
	}

	public String print() {
		return "nodeCount=" + nodeCount + "\nmaxLevel=" + maxLevel
				+ "\nexpansions=" + expansions + "\nexpansionSum="
				+ expansionSum + "\nexpansionFactorAvg=" + expansionFactorAvg
				+ "\ndensityAvg=" + densityAvg + "\ntermLevelAvg=" + termLevelAvg;
	}

}
