package br.ufsc.egc.curriculumextractor.approachs.selected;

import br.ufsc.egc.curriculumextractor.approachs.AbstractEntityCurriculumMatcher;
import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.TokenStatistics;
import br.ufsc.egc.curriculumextractor.model.entities.ApproachEntityRecognitionMetrics;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.dbpedia.reader.service.DBPediaService;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Eh hierarquico
public class CorpusCoocurrenceMatcher extends AbstractEntityCurriculumMatcher {

	private static final int DEFAULT_LEVELS = 3;
	private final DBPediaService dbPediaService;
	private int levels;

	public CorpusCoocurrenceMatcher(int levels) throws IOException {
		this.levels = levels;
		this.dbPediaService = getDBPediaService();
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}

	public int getLevels() {
		return levels;
	}

	public ApproachResponse createTree(TObjectIntMap<String> entitiesAndCount, int numberOfTokens, int recognizedTokens) throws IOException {

		Tree tree = new Tree();
		List<String> entities = new ArrayList<>(entitiesAndCount.keySet());
		Set<String> usedEntities = new HashSet<>();
		DBPediaService dbPedia = getDBPediaService();

		for (String entity1 : entities) {
			Term hierarchy = dbPedia.findTree(entity1, levels);
			for (String entity2 : entities) {
				if (!entity1.equalsIgnoreCase(entity2)) { // ignore same term
					Term result = hierarchy.find(entity2, true);
					if (result != null) {
						usedEntities.add(entity1);
						usedEntities.add(entity2);
						addHierarchy(tree, entity1, result);
					}
				}
			}
		}

		TObjectIntMap<String> usedEntitiesAndCount = new TObjectIntHashMap<>();

		for (String usedEntity : usedEntities) {
			usedEntitiesAndCount.put(usedEntity, entitiesAndCount.get(usedEntity));
		}

		TokenStatistics statistics = countUsedTokens(tree, usedEntitiesAndCount);
		ApproachEntityRecognitionMetrics nerMetrics = new ApproachEntityRecognitionMetrics(numberOfTokens, recognizedTokens, statistics.getUsedTokens(), usedEntities);
		return new ApproachResponse(tree, entities, nerMetrics, statistics.getCyclicWords());

	}

	private void addHierarchy(Tree tree, String sonLabel, Term fatherTerm) {
		while (fatherTerm.getParent() != null) {
			addToTree(tree, fatherTerm.getLabel(), fatherTerm.getParent().getLabel());
			fatherTerm = fatherTerm.getParent();
		}
		addToTree(tree, fatherTerm.getLabel(), sonLabel);
	}

}
