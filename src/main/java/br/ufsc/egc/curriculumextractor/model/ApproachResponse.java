package br.ufsc.egc.curriculumextractor.model;

import java.util.List;
import java.util.Set;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.NERMetrics;

public class ApproachResponse {

	private Tree tree;
	private List<String> discoveredEntities;
	private NERMetrics nerMetrics;
	private Set<String> cyclicWords;
	
	public ApproachResponse(Tree tree, List<String> discoveredEntities, NERMetrics nerMetrics, Set<String> cyclicTokens) {
		this.tree = tree;
		this.discoveredEntities = discoveredEntities;
		this.nerMetrics = nerMetrics;
		this.cyclicWords = cyclicTokens;
	}

	public Tree getTree() {
		return tree;
	}

	public List<String> getDiscoveredEntities() {
		return discoveredEntities;
	}
	
	public NERMetrics getNerMetrics() {
		return nerMetrics;
	}
	
	public Set<String> getCyclicWords() {
		return cyclicWords;
	}
	
}