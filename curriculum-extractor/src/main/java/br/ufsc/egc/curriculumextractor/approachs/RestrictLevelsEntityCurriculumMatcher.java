package br.ufsc.egc.curriculumextractor.approachs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufsc.egc.curriculumextractor.core.EntityImprover;
import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.TokenStatistics;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.NERMetrics;
import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface;
import br.ufsc.egc.dbpedia.reader.service.impl.DBPediaServiceImpl;

public class RestrictLevelsEntityCurriculumMatcher extends AbstractEntityCurriculumMatcher {

	public ApproachResponse createTree() throws RemoteException {

		EntityImprover improver = new EntityImprover();
		Map<String, Integer> entitiesCount = improver.getSortedEntitiesMap();

		entitiesCount = getFilteredMap(entitiesCount);
		
		List<String> entities = new ArrayList<String>(entitiesCount.keySet());

		DBPediaServiceInterface dbPediaService = DBPediaServiceImpl.getInstance();
		
		Tree tree = new Tree();
		
		for (int index = 0; index < entities.size(); index++) {
			String entity = entities.get(index);
			List<String> results = dbPediaService.findBroaderConceptsARQ(entity, 3);
			for (int innerIndex = 0; innerIndex < entities.size(); innerIndex++) {
				String innerEntity = entities.get(innerIndex);
				for (String result: results) {
					if (innerEntity.trim().equalsIgnoreCase(result.trim())) {
						addToTree(tree, innerEntity, entity);
					}
				}
			}
		}
		
		TokenStatistics statistics = countUsedTokens(tree);
		NERMetrics nerMetrics = new NERMetrics(improver.getNumberOfTokens(), improver.getRecognizedTokens(), statistics.getUsedTokens());
		return new ApproachResponse(tree, entities, nerMetrics, statistics.getCyclicWords());
		
	}
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		new RestrictLevelsEntityCurriculumMatcher().writeTree();
	}

}
