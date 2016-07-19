package br.ufsc.egc.curriculumextractor.batch;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.approachs.EntityCorpusCoocurrenceHierachicMatcher;

public class ApproachBatchExecutor {
	
	private static final int LEVELS_MAX = 3;
	private static final int ENTITY_THRESHOLD_MAX = 3;
	private static final Logger LOGGER = Logger.getLogger(ApproachBatchExecutor.class); 

	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		EntityCorpusCoocurrenceHierachicMatcher approach = new EntityCorpusCoocurrenceHierachicMatcher();
		
		for (int entityThreshold = 1; entityThreshold <= ENTITY_THRESHOLD_MAX; entityThreshold++) {
			approach.setEntityThreshold(entityThreshold);
			for (int levels = 1; levels <= LEVELS_MAX; levels++) {
				approach.setLevels(levels);
				LOGGER.info("Iniciando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
				approach.writeTree();
				LOGGER.info("Terminando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
			}
		}

	}

}
