package br.ufsc.egc.curriculumextractor.util;

import br.ufsc.egc.agrovoc.factory.AgrovocServiceFactoryImpl;
import br.ufsc.egc.agrovoc.service.AgrovocService;
import br.ufsc.egc.curriculumextractor.model.entities.ApproachEntityRecognitionMetrics;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class TreeMetrics {

	private int nodeCount = 0;
	private int maxLevel = 0;
	private int expansions = 0;
	private int expansionSum = 0;
	private double expansionFactorAvg;
	private int termsFoundInAgrovoc = 0;
	private double agrovocNameMatching;
	private double termLevelAvg;
	private int termLevelSum = 0;
	private Set<String> cyclicWords;
	private int cyclicWordsNumber;
	private double cyclicWordsFactor;
	private double horizontality;
	private double verticality;
	private TObjectIntHashMap<String> usedTerms;

	// servico

	private ApproachEntityRecognitionMetrics nerMetrics;

	private AgrovocService agrovocService;

	public TreeMetrics(Tree tree, ApproachEntityRecognitionMetrics nerMetrics, Set<String> cyclicWords) throws IOException {
		this.cyclicWords = cyclicWords;
		this.agrovocService = new AgrovocServiceFactoryImpl().buildFromProperties();
		this.nerMetrics = nerMetrics;
		calculateSums(tree);
		calculateStatistics();
		summarizeAllUsedTerms(tree);
	}

	private void summarizeAllUsedTerms(Tree tree) {
		usedTerms = new TObjectIntHashMap<String>();
		summarizeAllUsedTerms(usedTerms, tree);
	}

	private void summarizeAllUsedTerms(TObjectIntHashMap<String> usedTerms, Tree tree) {
		for (Term root: tree.getRoots()) {
			summarizeAllUsedTerms(usedTerms, root);
		}
	}

	private void summarizeAllUsedTerms(TObjectIntHashMap<String> usedTerms, Term term) {
		usedTerms.adjustOrPutValue(term.getLabel(), 1, 1);
		for (Term son: term.getSons()) {
			summarizeAllUsedTerms(usedTerms, son);
		}
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

	public String print() {
		DecimalFormat df = new DecimalFormat("0.000");
		
		StringBuilder sbUsedTerms = new StringBuilder();
		List<String> orderedUsedTerms = new ArrayList<String>();
		orderedUsedTerms.addAll(usedTerms.keySet());
		Collections.sort(orderedUsedTerms);
		Iterator<String> itOrderedUsedTerms = orderedUsedTerms.iterator();
		
		while (itOrderedUsedTerms.hasNext()) {
			String term = itOrderedUsedTerms.next();
			sbUsedTerms.append(term);
			sbUsedTerms.append("[" + usedTerms.get(term) +"]");
			if (itOrderedUsedTerms.hasNext()) {
				sbUsedTerms.append(", ");
			}
		}
		
		
		return "nodeCount=" + nodeCount 
				+ "\nmaxLevel=" + maxLevel 
				+ "\nexpansions=" + expansions 
				+ "\nexpansionSum=" + expansionSum 
				+ "\nexpansionFactorAvg=" + df.format(expansionFactorAvg)
				+ "\ntermLevelAvg=" + df.format(termLevelAvg) 
				+ "\ncyclicWordsNumber=" + cyclicWordsNumber 
				+ "\ncyclicWordsFactor=" + df.format(cyclicWordsFactor) 
				+ "\nhorizontality=" + df.format(horizontality) 
				+ "\nverticality=" + df.format(verticality)
				+ "\n\ntermsFoundInAgrovoc=" + termsFoundInAgrovoc
				+ "\nagrovocNameMatching=" + df.format(agrovocNameMatching)
				+ "\nusedTerms=" + sbUsedTerms +
				
				"\n\n" + nerMetrics.getUsedTokens()
				+ "\n" + df.format(nerMetrics.getUsedTokensFactor())
				+ "\n" + nodeCount 
				+ "\n" + maxLevel 
				+ "\n" + expansions 
				+ "\n" + expansionSum 
				+ "\n" + df.format(expansionFactorAvg)
				+ "\n" + df.format(termLevelAvg) 
				+ "\n" + cyclicWordsNumber 
				+ "\n" + df.format(cyclicWordsFactor) 
				+ "\n" + df.format(horizontality) 
				+ "\n" + df.format(verticality)
				+ "\n" + termsFoundInAgrovoc
				+ "\n" + df.format(agrovocNameMatching) +
		
				"\n\n" + nerMetrics.getUsedTokens()
				+ "\t" + df.format(nerMetrics.getUsedTokensFactor())
				+ "\t" + nodeCount 
				+ "\t" + maxLevel 
				+ "\t" + expansions 
				+ "\t" + expansionSum 
				+ "\t" + df.format(expansionFactorAvg)
				+ "\t" + df.format(termLevelAvg) 
				+ "\t" + cyclicWordsNumber 
				+ "\t" + df.format(cyclicWordsFactor) 
				+ "\t" + df.format(horizontality) 
				+ "\t" + df.format(verticality)
				+ "\t" + termsFoundInAgrovoc
				+ "\t" + df.format(agrovocNameMatching);
	}

}
