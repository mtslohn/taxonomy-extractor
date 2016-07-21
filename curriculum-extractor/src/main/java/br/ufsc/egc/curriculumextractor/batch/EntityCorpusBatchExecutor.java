package br.ufsc.egc.curriculumextractor.batch;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.approachs.selected.CurriculumCoocurrenceMatcher;
import br.ufsc.egc.curriculumextractor.core.NewEntityExtractor;
import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.NewEntityExtractorUtils;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;
import gnu.trove.map.TObjectIntMap;

public class EntityCorpusBatchExecutor {
	
	private static final int READ_LINES_LIMIT = 15000;
	private static final int LEVELS_MAX = 3;
	private static final int ENTITY_THRESHOLD_MIN = 300; // READ_LINES_LIMIT / 50
	private static final int ENTITY_THRESHOLD_MAX = 400;
	private static final int ENTITY_ITERATION = 25;

	private static final Logger LOGGER = Logger.getLogger(EntityCorpusBatchExecutor.class); 

	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		NewEntityExtractor nee = new NewEntityExtractor();
		TObjectIntMap<String> entitiesAndCount = nee.recognizeAndExtract(READ_LINES_LIMIT);
		CurriculumCoocurrenceMatcher approach = new CurriculumCoocurrenceMatcher(READ_LINES_LIMIT);
		
		for (int entityThreshold = ENTITY_THRESHOLD_MAX; entityThreshold >= ENTITY_THRESHOLD_MIN; entityThreshold = entityThreshold - ENTITY_ITERATION) {
			TObjectIntMap<String> entitiesAndCountFilteredMap = NewEntityExtractorUtils.filterByEntityThreshold(entitiesAndCount, entityThreshold);
			approach.prepareForEntities(entitiesAndCountFilteredMap);
			for (int levels = 1; levels <= LEVELS_MAX; levels++) {

				approach.setLevels(levels);
				
				LOGGER.info("Iniciando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
				
				ApproachResponse approachResponse = approach.createTree(nee.getNumberOfTokens(), nee.getRecognizedTokens());
				Tree tree = approachResponse.getTree();
				TreeWriter treeWriter = new TreeWriter();
				String fileName = String.format("Curriculum Coocurrence - %s linhas lidas - %s entityThreshold - %s levels", READ_LINES_LIMIT, entityThreshold, levels);
				treeWriter.write(fileName, approachResponse.getNerMetrics(), approachResponse.getCyclicTokens(), tree);
				
				LOGGER.info("Terminando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
			}
		}

	}

}
