package br.ufsc.egc.curriculumextractor.approachs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufsc.egc.curriculumextractor.core.EntityImprover;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.dbpedia.reader.service.DBPediaService;

public class RestrictEntityHierachicCleanedCurriculumMatcher extends AbstractEntityCurriculumMatcher {

	private static final int LEVELS = 9;

	public void process() {

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
				Term result = hierarchy.find(entitiesList.get(innerIndex), true);
				if (result != null) {
					while (result.getParent() != null) {
						addToTree(tree, result.getLabel(), result.getParent().getLabel());
						result = result.getParent();
					}
					addToTree(tree, innerEntity, entity);
					
				}
			}
		}
		
		// limpeza da arvore
		Tree newTree = tree.clean(entitiesList);
		
		System.out.println(newTree.print());
		
		
	}
	
	public static void main(String[] args) {
		new RestrictEntityHierachicCleanedCurriculumMatcher().process();		
	}

}
