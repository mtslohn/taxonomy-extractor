package br.ufsc.egc.curriculumextractor.approachs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufsc.egc.curriculumextractor.core.EntityImprover;
import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface;
import br.ufsc.egc.dbpedia.reader.service.impl.DBPediaServiceImpl;

public class RestrictEntityCurriculumMatcher extends AbstractEntityCurriculumMatcher {

	public ApproachResponse createTree() throws RemoteException {

		EntityImprover improver = new EntityImprover();
		Map<String, Integer> entitiesCount = improver.getSortedEntitiesMap();

		entitiesCount = getFilteredMap(entitiesCount);
		
		List<String> entitiesList = new ArrayList<String>(entitiesCount.keySet());

		DBPediaServiceInterface dbPediaService = DBPediaServiceImpl.getInstance();
		
		Tree tree = new Tree();
		
		for (int index = 0; index < entitiesList.size(); index++) {
			String entity = entitiesList.get(index);
			List<String> results = dbPediaService.findBroaderConcepts(entity);
			for (int innerIndex = 0; innerIndex < entitiesList.size(); innerIndex++) {
				String innerEntity = entitiesList.get(innerIndex);
				for (String result: results) {
					if (innerEntity.trim().equalsIgnoreCase(result.trim())) {
						addToTree(tree, innerEntity, entity);
					}
				}
			}
		}
		
		return new ApproachResponse(tree, entitiesList);
		
	}
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		new RestrictEntityCurriculumMatcher().writeTree();
	}

}
