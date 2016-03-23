package br.ufsc.egc.curriculumextractor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ufsc.egc.curriculumextractor.model.CurriculumCorrelation;
import br.ufsc.egc.curriculumextractor.model.EntityPair;
import br.ufsc.egc.curriculumextractor.model.EntityPairCoocurrence;
import br.ufsc.egc.curriculumextractor.model.EntityPairCoocurrenceManager;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.dbpedia.reader.service.DBPediaService;

public class EntityCurriculumMatcher {

	public static void main(String[] args) {

		EntityImprover improver = new EntityImprover();
		Map<String, Integer> entitiesCount = improver.getSortedEntitiesMap();

		entitiesCount = getFilteredMap(entitiesCount);

		CurriculumListReader curriculumListReader = new CurriculumListReader();

		Map<Integer, String> curriculumMap = curriculumListReader.read();
		List<String> entities = new ArrayList<String>(entitiesCount.keySet());
		
		List<CurriculumCorrelation> correlations = new ArrayList<CurriculumCorrelation>();

		for (Integer curriculumKey : curriculumMap.keySet()) {

			String curriculum = curriculumMap.get(curriculumKey);

			for (int indexOuter = 0; indexOuter < entities.size(); indexOuter++) {
				String entityOuter = entities.get(indexOuter);

				if (curriculum.contains(entityOuter)) {

					for (int indexInner = indexOuter + 1; indexInner < entities
							.size(); indexInner++) {
						String entityInner = entities.get(indexInner);
						CurriculumCorrelation correlation = new CurriculumCorrelation();
						correlation.setCurriculumId(curriculumKey);

						if (curriculum.contains(entityInner)) {
							EntityPair pair = new EntityPair();
							pair.setEntity1(entityOuter);
							pair.setEntity2(entityInner);
							correlation.getPairs().add(pair);
						}
						
						if (!correlation.getPairs().isEmpty()) {
							correlations.add(correlation);
						}
						
					}
				}

			}

		}
		
		EntityPairCoocurrenceManager manager = new EntityPairCoocurrenceManager();
		
		for (CurriculumCorrelation correlation: correlations) {
			for (EntityPair pair: correlation.getPairs()) {
				manager.addPair(pair.getEntity1(), pair.getEntity2());
			}
		}
		
		// ja tenho as relacoes e a frequencia delas... agora eh hora de montar a arvore
		// TODO colocar na tree as relacoes descobertas e validadas na DBPedia
		
		DBPediaService dbPediaService = DBPediaService.getInstance();
		
		Tree tree = new Tree();
		
		for (EntityPairCoocurrence pair : manager.getPairs()) {
			List<String> broadersEntity1 = dbPediaService.findAllNarrowConcepts(pair.getEntity1());
			if (broadersEntity1.contains(pair.getEntity2())) {
				System.out.println("ACHOU");
			}
			List<String> broadersEntity2 = dbPediaService.findAllNarrowConcepts(pair.getEntity2());
			if (broadersEntity2.contains(pair.getEntity1())) {
				System.out.println("ACHOU");
			}
		}
		
	}

	private static Map<String, Integer> getFilteredMap(
			Map<String, Integer> entitiesCount) {

		LinkedHashMap<String, Integer> entitiesCountCleanMap = new LinkedHashMap<String, Integer>();

		for (String key : entitiesCount.keySet()) {
			if (entitiesCount.get(key) == 2) {
				break;
			}
			entitiesCountCleanMap.put(key, entitiesCount.get(key));
		}

		return entitiesCountCleanMap;

	}

}
