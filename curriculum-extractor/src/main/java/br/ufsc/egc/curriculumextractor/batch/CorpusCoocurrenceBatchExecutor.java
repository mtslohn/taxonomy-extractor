package br.ufsc.egc.curriculumextractor.batch;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.approachs.selected.CorpusCoocurrenceMatcher;
import br.ufsc.egc.curriculumextractor.batch.entities.EntityReader;
import br.ufsc.egc.curriculumextractor.core.NewEntityExtractor;
import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.json.util.JsonNodeWriter;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.NewEntityExtractorUtils;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;
import br.ufsc.egc.curriculumextractor.util.constants.EntityExtractorConstants;
import gnu.trove.map.TObjectIntMap;

public class CorpusCoocurrenceBatchExecutor {
	
	private static final int READ_LINES_LIMIT = EntityExtractorConstants.READ_LINES_LIMIT;
	
	private static final boolean EXTRACT_ENTITIES_ON_RUNTIME = false;
	
	private static final int[] ENTITY_THRESHOLD_ARRAY = {1000, 800, 500, 300, 100};
	private static final int[] LEVELS_ARRAY = {1, 2, 3};

	private static final Logger LOGGER = Logger.getLogger(CorpusCoocurrenceBatchExecutor.class); 

	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		TObjectIntMap<String> entitiesAndCount;
		int numberOfTokens;
		int recognizedTokens;
		
		if (EXTRACT_ENTITIES_ON_RUNTIME) {
			NewEntityExtractor nee = new NewEntityExtractor();
			entitiesAndCount = nee.recognizeAndExtract(READ_LINES_LIMIT);
			numberOfTokens = nee.getNumberOfTokens();
			recognizedTokens = nee.getRecognizedTokens();
		}
		else {
			EntityReader entityReader = new EntityReader();
			entityReader.readEntities();
			entitiesAndCount = entityReader.getEntitiesAndCount();
			numberOfTokens = entityReader.getNumberOfTokens();
			recognizedTokens = entityReader.getRecognizedTokens();
			
		}
		CorpusCoocurrenceMatcher approach = new CorpusCoocurrenceMatcher(READ_LINES_LIMIT);
		
		for (int entityThreshold : ENTITY_THRESHOLD_ARRAY) {
			TObjectIntMap<String> entitiesAndCountFilteredMap = NewEntityExtractorUtils.filterByEntityThreshold(entitiesAndCount, entityThreshold);
			for (int levels : LEVELS_ARRAY) {

				approach.setLevels(levels);
				
				LOGGER.info("Iniciando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
				
				ApproachResponse approachResponse = approach.createTree(entitiesAndCountFilteredMap, numberOfTokens, recognizedTokens);
				Tree tree = approachResponse.getTree();
				TreeWriter treeWriter = new TreeWriter();
				String fileName = String.format("Corpus - %s linhas lidas - %s entityThreshold - %s levels", READ_LINES_LIMIT, entityThreshold, levels);
				treeWriter.write(fileName, approachResponse.getNerMetrics(), approachResponse.getCyclicWords(), tree);
				JsonNodeWriter jsonWriter = new JsonNodeWriter();
				jsonWriter.writeTree(fileName, tree);
				
				LOGGER.info("Terminando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
			}
		}

	}

}
