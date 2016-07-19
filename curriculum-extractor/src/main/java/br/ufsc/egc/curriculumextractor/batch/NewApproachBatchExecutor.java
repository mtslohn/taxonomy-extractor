package br.ufsc.egc.curriculumextractor.batch;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.approachs.RestrictEntityHierachicCurriculumMatcher;
import br.ufsc.egc.curriculumextractor.core.NewEntityExtractor;
import br.ufsc.egc.curriculumextractor.util.NewEntityExtractorUtils;
import gnu.trove.map.TObjectIntMap;

public class NewApproachBatchExecutor {
	
	private static final int LEVELS_MAX = 3;
	private static final int ENTITY_THRESHOLD_MIN = 1000;
	private static final int ENTITY_THRESHOLD_MAX = 1000;
	private static final int ENTITY_ITERATION = 5;

	private static final Logger LOGGER = Logger.getLogger(NewApproachBatchExecutor.class); 

	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		NewEntityExtractor nee = new NewEntityExtractor();
		
		// TODO filtrar o hashmap de entidades pelo numero do entity threshold
		TObjectIntMap<String> entitiesAndCount = nee.recognizeAndExtract(3000);
		
		RestrictEntityHierachicCurriculumMatcher approach = new RestrictEntityHierachicCurriculumMatcher();
		
		for (int entityThreshold = ENTITY_THRESHOLD_MIN; entityThreshold <= ENTITY_THRESHOLD_MAX; entityThreshold = entityThreshold + ENTITY_ITERATION) {
			
			Set<String> entities = NewEntityExtractorUtils.filterByEntityThreshold(entitiesAndCount, entityThreshold);
			
			// FIXME depreciado
			approach.setEntityThreshold(entityThreshold);
			
			for (int levels = 1; levels <= LEVELS_MAX; levels++) {
				approach.setLevels(levels);
				LOGGER.info("Iniciando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
				approach.writeTree(new ArrayList<String>(entities), nee.getNumberOfTokens(), nee.getRecognizedTokens());
				LOGGER.info("Terminando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
			}
		}

	}

}
