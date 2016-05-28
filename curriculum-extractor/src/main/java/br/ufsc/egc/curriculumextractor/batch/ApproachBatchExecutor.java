package br.ufsc.egc.curriculumextractor.batch;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.approachs.RestrictEntityHierachicCurriculumMatcher;

public class ApproachBatchExecutor {
	
	private static final Logger LOGGER = Logger.getLogger(ApproachBatchExecutor.class); 

	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		RestrictEntityHierachicCurriculumMatcher approach = new RestrictEntityHierachicCurriculumMatcher();
		
		for (int entityThreshold = 1; entityThreshold <= 10; entityThreshold++) {
			approach.setEntityThreshold(entityThreshold);
			for (int levels = 1; levels <= 3; levels++) {
				approach.setLevels(levels);
				LOGGER.info("Iniciando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
				approach.writeTree();
				LOGGER.info("Terminando execução com entityThreshold de " + entityThreshold + " e levels de " + levels + ".");
			}
		}

	}

}
