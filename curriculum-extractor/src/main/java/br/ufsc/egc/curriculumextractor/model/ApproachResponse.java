package br.ufsc.egc.curriculumextractor.model;

import java.util.List;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;

public class ApproachResponse {

	private Tree tree;
	private List<String> discoveredEntities;
	
	public ApproachResponse(Tree tree, List<String> discoveredEntities) {
		this.tree = tree;
		this.discoveredEntities = discoveredEntities;
	}

	public Tree getTree() {
		return tree;
	}

	public List<String> getDiscoveredEntities() {
		return discoveredEntities;
	}

}
