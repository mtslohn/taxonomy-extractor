package br.ufsc.egc.curriculumextractor.approachs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.CurriculumListReader;
import br.ufsc.egc.curriculumextractor.core.EntityImprover;
import br.ufsc.egc.curriculumextractor.model.CurriculumCorrelation;
import br.ufsc.egc.curriculumextractor.model.EntityPair;
import br.ufsc.egc.curriculumextractor.model.EntityPairCoocurrenceManager;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;
import br.ufsc.egc.dbpedia.reader.service.DBPediaService;

public class EntityCurriculumHierarchicCoocurrenceMatcher extends
		AbstractEntityCurriculumMatcher {

	private static final Logger LOGGER = Logger
			.getLogger(EntityCurriculumHierarchicCoocurrenceMatcher.class);
	
	private static final int LEVELS = 9;

	public Tree createTree() {

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

		for (int index = 0; index < correlations.size(); index++) {
			if (index % 1000 == 0) {
				LOGGER.info("Processando " + index);
			}
			CurriculumCorrelation correlation = correlations.get(index);
			for (EntityPair pair : correlation.getPairs()) {
				manager.addPair(pair.getEntity1(), pair.getEntity2());
			}
		}

		// ja tenho as relacoes e a frequencia delas... agora eh hora de montar
		// a arvore
		// TODO colocar na tree as relacoes descobertas e validadas na DBPedia

		DBPediaService dbPediaService = DBPediaService.getInstance();

		Tree tree = new Tree();

		for (EntityPair pair : manager.getPairsCoocurrence().keySet()) {
			Term hierarchy1 = dbPediaService
					.findTree(pair.getEntity1(), LEVELS);
			if (hierarchy1 != null) {
				Term result1 = hierarchy1.find(pair.getEntity2(), true);
				if (result1 != null) {
					while (result1.getParent() != null) {
						addToTree(tree, result1.getLabel(), result1.getParent()
								.getLabel());
						result1 = result1.getParent();
					}
					addToTree(tree, result1.getLabel(), pair.getEntity1());

				}
			}
			if (hierarchy1.find(pair.getEntity2(), true) != null) {
				tree.addToTree(pair.getEntity2(), pair.getEntity1());
			}
			}
			List<String> broadersEntity2 = dbPediaService
					.findBroaderConcepts(pair.getEntity2());
			if (broadersEntity2.contains(pair.getEntity1())) {
				tree.addToTree(pair.getEntity1(), pair.getEntity2());
			}
		}
		
		return tree;
		
	}
	
	public static void main(String[] args) {
		new EntityCurriculumHierarchicCoocurrenceMatcher().writeTree();
	}

}
