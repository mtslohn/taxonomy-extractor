package br.ufsc.egc.curriculumextractor.batch;

import br.ufsc.egc.curriculumextractor.approachs.selected.CorpusCoocurrenceMatcher;
import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.entities.EntityRecognitionData;
import br.ufsc.egc.curriculumextractor.model.json.util.JsonNodeWriter;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.NewEntityExtractorUtils;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;
import br.ufsc.egc.curriculumextractor.util.constants.EntityExtractorConstants;
import gnu.trove.map.TObjectIntMap;
import org.apache.log4j.Logger;

import java.io.IOException;

import static br.ufsc.egc.curriculumextractor.batch.EntityRecognitionHelper.loadEntities;

public class CorpusCoocurrenceBatchExecutor {

	private static final String PATH_PREFIX = "corpus";

	private static final int READ_LINES_LIMIT = EntityExtractorConstants.READ_LINES_LIMIT;

	private static final boolean EXTRACT_ENTITIES_ON_RUNTIME = false;

	private static final int[] ENTITY_THRESHOLD_ARRAY = {1000, 800, 500, 300, 100};
	private static final int[] LEVELS_ARRAY = {1, 2, 3};

	private static final Logger LOGGER = Logger.getLogger(CorpusCoocurrenceBatchExecutor.class);

	public static void main(String[] args) throws IOException {

		CorpusCoocurrenceMatcher approach = new CorpusCoocurrenceMatcher(READ_LINES_LIMIT); //FIXME
		EntityRecognitionData nerData = loadEntities(EXTRACT_ENTITIES_ON_RUNTIME, READ_LINES_LIMIT);

		for (int entityThreshold : ENTITY_THRESHOLD_ARRAY) {
			TObjectIntMap<String> entitiesAndCountFilteredMap = NewEntityExtractorUtils.filterByEntityThreshold(nerData.getEntityCountMap(), entityThreshold);
			for (int levels : LEVELS_ARRAY) {

				approach.setLevels(levels);

				LOGGER.info("Iniciando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");

				ApproachResponse approachResponse = approach.createTree(entitiesAndCountFilteredMap, nerData.getNumberOfTokens(), nerData.getRecognizedTokens());
				Tree tree = approachResponse.getTree();
				TreeWriter treeWriter = new TreeWriter();

				String fileName = String.format("%s lines - %s entityThreshold - %s levels", READ_LINES_LIMIT, entityThreshold, levels);
				treeWriter.write(PATH_PREFIX, fileName, approachResponse.getNerMetrics(), approachResponse.getCyclicWords(), tree);
				JsonNodeWriter jsonWriter = new JsonNodeWriter();
				jsonWriter.writeTree(PATH_PREFIX, fileName, tree);

				LOGGER.info("Terminando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
			}
		}

	}

}
