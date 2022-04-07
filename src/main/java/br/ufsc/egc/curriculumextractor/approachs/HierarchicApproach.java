package br.ufsc.egc.curriculumextractor.approachs;

import gnu.trove.map.TObjectIntMap;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface HierarchicApproach {

	int getLevels();

	void writeTree(int entityThreshold, TObjectIntMap<String> entitiesAndCount, int numberOfTokens,
	               int recognizedTokens) throws RemoteException, NotBoundException;

}
