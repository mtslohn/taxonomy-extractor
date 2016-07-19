package br.ufsc.egc.curriculumextractor.approachs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public interface HierarchicApproach {
	
	int getLevels();
	
	void writeTree(List<String> entities, int numberOfTokens, int recognizedTokens) throws RemoteException, NotBoundException; 

}
