package br.ufsc.egc.curriculumextractor.approachs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufsc.egc.curriculumextractor.core.EntityImprover;
import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.TokenStatistics;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.NERMetrics;
import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface;
import br.ufsc.egc.dbpedia.reader.service.impl.DBPediaServiceImpl;

public class RestrictEntityHierachicCleanedCurriculumMatcher extends AbstractEntityCurriculumMatcher implements HierarchicApproach {

	private static final int LEVELS = 3;
	
	public int getLevels() {
		return LEVELS;
	}

	public ApproachResponse createTree() throws RemoteException {

		EntityImprover improver = new EntityImprover();
		Map<String, Integer> entitiesCount = improver.getSortedEntitiesMap();

		entitiesCount = getFilteredMap(entitiesCount);
		
		List<String> entities = new ArrayList<String>(entitiesCount.keySet());

		DBPediaServiceInterface dbPediaService = DBPediaServiceImpl.getInstance();
		
		Tree tree = new Tree();
		
		for (int index = 0; index < entities.size(); index++) {
			String entity = entities.get(index);
			Term hierarchy = dbPediaService.findTree(entity, LEVELS);
			for (int innerIndex = 0; innerIndex < entities.size(); innerIndex++) {
				String innerEntity = entities.get(innerIndex);
				Term result = hierarchy.find(innerEntity, true);
				if (result != null) {
					addHierarchy(tree, entity, result);
					
				}
			}
		}
		
		// limpeza da arvore
		Tree newTree = tree.clean(entities);
		
		TokenStatistics statistics = countUsedTokens(tree);
		NERMetrics nerMetrics = new NERMetrics(improver.getNumberOfTokens(), improver.getRecognizedTokens(), statistics.getUsedTokens());
		return new ApproachResponse(newTree, entities, nerMetrics, statistics.getCyclicWords());
		
		
	}

	private void addHierarchy(Tree tree, String sonLabel, Term fatherTerm) {
		while (fatherTerm.getParent() != null) {
			addToTree(tree, fatherTerm.getLabel(), fatherTerm.getParent().getLabel());
			fatherTerm = fatherTerm.getParent();
		}
		addToTree(tree, fatherTerm.getLabel(), sonLabel);
	}
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		new RestrictEntityHierachicCleanedCurriculumMatcher().writeTree();		
	}

}
