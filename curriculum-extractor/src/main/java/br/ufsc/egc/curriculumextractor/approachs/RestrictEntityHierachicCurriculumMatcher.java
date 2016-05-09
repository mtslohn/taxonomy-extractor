package br.ufsc.egc.curriculumextractor.approachs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufsc.egc.curriculumextractor.core.EntityImprover;
import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.dbpedia.reader.service.DBPediaService;

public class RestrictEntityHierachicCurriculumMatcher extends AbstractEntityCurriculumMatcher {

	private static final int LEVELS = 9;

	public ApproachResponse createTree() {

		EntityImprover improver = new EntityImprover();
		Map<String, Integer> entitiesCount = improver.getSortedEntitiesMap();

		entitiesCount = getFilteredMap(entitiesCount);
		
		List<String> entitiesList = new ArrayList<String>(entitiesCount.keySet());

		DBPediaService dbPediaService = DBPediaService.getInstance();
		
		Tree tree = new Tree();
		
		for (int index = 0; index < entitiesList.size(); index++) {
			String entity = entitiesList.get(index);
			Term hierarchy = dbPediaService.findTree(entity, LEVELS);
			for (int innerIndex = 0; innerIndex < entitiesList.size(); innerIndex++) {
				String innerEntity = entitiesList.get(innerIndex);
				Term result = hierarchy.find(innerEntity, true);
				if (result != null) {
					addHierarchy(tree, entity, result);
				}
			}
		}
		
		return new ApproachResponse(tree, entitiesList);
		
	}

	private void addHierarchy(Tree tree, String sonLabel, Term fatherTerm) {
		while (fatherTerm.getParent() != null) {
			addToTree(tree, fatherTerm.getLabel(), fatherTerm.getParent().getLabel());
			fatherTerm = fatherTerm.getParent();
		}
		addToTree(tree, fatherTerm.getLabel(), sonLabel);
	}
	
	public static void main(String[] args) {
		new RestrictEntityHierachicCurriculumMatcher().writeTree();
	}

}
