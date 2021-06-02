package br.ufsc.egc.curriculumextractor.approachs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import gnu.trove.map.TObjectIntMap;

public interface HierarchicApproach {
	
	int getLevels();
	
	void writeTree(int entityThreshold, TObjectIntMap<String> entitiesAndCount, int numberOfTokens,
			int recognizedTokens) throws RemoteException, NotBoundException; 

}
