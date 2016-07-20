package br.ufsc.egc.curriculumextractor.batch;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.approachs.selected.EntityCurriculumCoocurrenceHierarchicMatcher;
import br.ufsc.egc.curriculumextractor.core.NewEntityExtractor;
import br.ufsc.egc.curriculumextractor.util.NewEntityExtractorUtils;
import gnu.trove.map.TObjectIntMap;

public class NewApproachBatchExecutor {
	
	private static final int READ_LINES_LIMIT = 3000;
	private static final int LEVELS_MAX = 3;
	private static final int ENTITY_THRESHOLD_MIN = 50;
	private static final int ENTITY_THRESHOLD_MAX = 100;
	private static final int ENTITY_ITERATION = 25;

	private static final Logger LOGGER = Logger.getLogger(NewApproachBatchExecutor.class); 

	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		NewEntityExtractor nee = new NewEntityExtractor();
		
		TObjectIntMap<String> entitiesAndCount = nee.recognizeAndExtract(READ_LINES_LIMIT);
		
		EntityCurriculumCoocurrenceHierarchicMatcher approach = new EntityCurriculumCoocurrenceHierarchicMatcher(READ_LINES_LIMIT);
		
		for (int entityThreshold = ENTITY_THRESHOLD_MIN; entityThreshold <= ENTITY_THRESHOLD_MAX; entityThreshold = entityThreshold + ENTITY_ITERATION) {
			
			TObjectIntMap<String> entitiesAndCountFilteredMap = NewEntityExtractorUtils.filterByEntityThreshold(entitiesAndCount, entityThreshold);
			
			for (int levels = 1; levels <= LEVELS_MAX; levels++) {
				approach.setLevels(levels);
				LOGGER.info("Iniciando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
				approach.writeTree(entityThreshold, entitiesAndCountFilteredMap, nee.getNumberOfTokens(), nee.getRecognizedTokens());
				LOGGER.info("Terminando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
			}
		}

	}

}
