package br.ufsc.egc.curriculumextractor.approachs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufsc.egc.curriculumextractor.core.EntityImprover;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.dbpedia.reader.service.DBPediaService;

public class FullEntityCurriculumMatcher extends AbstractEntityCurriculumMatcher {

	public static void main(String[] args) {

		EntityImprover improver = new EntityImprover();
		Map<String, Integer> entitiesCount = improver.getSortedEntitiesMap();

		entitiesCount = getFilteredMap(entitiesCount);
		
		List<String> entitiesList = new ArrayList<String>(entitiesCount.keySet());

		DBPediaService dbPediaService = DBPediaService.getInstance();
		
		Tree tree = new Tree();
		
		for (int index = 0; index < entitiesList.size(); index++) {
			String entity = entitiesList.get(index);
			List<String> results = dbPediaService.findAllBroaderConcepts(entity);
			for (int innerIndex = 0; innerIndex < entitiesList.size(); innerIndex++) {
				String innerEntity = entitiesList.get(innerIndex);
				for (String result: results) {
					if (innerEntity.trim().equalsIgnoreCase(result.trim())) {
						addToTree(tree, innerEntity, entity);
					}
				}
			}
		}
		
		System.out.println(tree.print());
		
	}

}
