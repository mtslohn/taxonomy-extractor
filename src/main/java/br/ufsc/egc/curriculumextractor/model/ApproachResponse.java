package br.ufsc.egc.curriculumextractor.model;

import br.ufsc.egc.curriculumextractor.model.entities.ApproachEntityRecognitionMetrics;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;

import java.util.List;
import java.util.Set;

public class ApproachResponse {

	private Tree tree;
	private List<String> discoveredEntities;
	private ApproachEntityRecognitionMetrics nerMetrics;
	private Set<String> cyclicWords;

	public ApproachResponse(Tree tree, List<String> discoveredEntities, ApproachEntityRecognitionMetrics nerMetrics, Set<String> cyclicTokens) {
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

	public ApproachEntityRecognitionMetrics getNerMetrics() {
		return nerMetrics;
	}

	public Set<String> getCyclicWords() {
		return cyclicWords;
	}

}