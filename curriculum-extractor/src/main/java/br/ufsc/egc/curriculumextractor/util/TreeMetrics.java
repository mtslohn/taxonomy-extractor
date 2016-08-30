package br.ufsc.egc.curriculumextractor.util;

import java.text.DecimalFormat;
import java.util.Set;

import br.ufsc.egc.agrovoc.service.AgrovocService;
import br.ufsc.egc.agrovoc.service.AgrovocServiceTest;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;

public class TreeMetrics {

	private int nodeCount = 0;
	private int maxLevel = 0;
	private int expansions = 0;
	private int expansionSum = 0;
	private double expansionFactorAvg;
	private int termsFoundInAgrovoc = 0;
	private double agrovocNameMatching;
	
	// TODO remover esse parametro
	// nao eh utilizado
	private double densityAvg;
	
	private double termLevelAvg;
	private int termLevelSum = 0;
	private Set<String> cyclicWords;
	private int cyclicWordsNumber;
	private double cyclicWordsFactor;
	private double horizontality;
	private double verticality;
	
	// servico
	
	private AgrovocService agrovocService;

	public TreeMetrics(Tree tree, Set<String> cyclicWords) {
		this.cyclicWords = cyclicWords;
		this.agrovocService = AgrovocService.getInstance();
		calculateSums(tree);
		calculateStatistics();
	}

	private void calculateStatistics() {
		expansionFactorAvg = expansionSum / (double) expansions;
		termLevelAvg = termLevelSum / (double) nodeCount;
		cyclicWordsNumber = cyclicWords.size();
		cyclicWordsFactor = cyclicWordsNumber / (nodeCount * 1.0);
		horizontality = maxLevel / (nodeCount * 1.0);
		verticality = nodeCount / (maxLevel * 1.0);
		agrovocNameMatching = termsFoundInAgrovoc*100.0/nodeCount;
	}

	private void calculateSums(Tree tree) {
		for (Term term : tree.getRoots()) {
			calculate(term, 1);
		}
	}

	private void calculate(Term term, int level) {
		nodeCount++;
		if (agrovocService.verifyIfExistsLabel(term.getLabel())) {
			termsFoundInAgrovoc++;
		}
		if (level > maxLevel) {
			maxLevel = level;
		}
		termLevelSum += level;
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
		DecimalFormat df = new DecimalFormat("0.000");
		return "nodeCount=" + nodeCount 
				+ "\nmaxLevel=" + maxLevel 
				+ "\nexpansions=" + expansions 
				+ "\nexpansionSum=" + expansionSum 
				+ "\nexpansionFactorAvg=" + df.format(expansionFactorAvg)
				+ "\ndensityAvg=" + df.format(densityAvg)
				+ "\ntermLevelAvg=" + df.format(termLevelAvg) 
				+ "\ncyclicWordsNumber=" + cyclicWordsNumber 
				+ "\ncyclicWordsFactor=" + df.format(cyclicWordsFactor) 
				+ "\nhorizontality=" + df.format(horizontality) 
				+ "\nverticality=" + df.format(verticality)
				+ "\ntermsFoundInAgrovoc=" + termsFoundInAgrovoc
				+ "\nagrovocNameMatching=" + df.format(agrovocNameMatching);
	}

}
