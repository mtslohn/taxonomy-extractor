package br.ufsc.egc.curriculumextractor.approachs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufsc.egc.curriculumextractor.core.EntityImprover;
import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface;

public class RestrictEntityHierachicCurriculumMatcher extends AbstractEntityCurriculumMatcher implements HierarchicApproach {

	private static final int LEVELS = 3;
	
	public int getLevels() {
		return LEVELS;
	}

	public ApproachResponse createTree() throws RemoteException, NotBoundException {

		EntityImprover improver = new EntityImprover();
		Map<String, Integer> entitiesCount = improver.getSortedEntitiesMap();

		entitiesCount = getFilteredMap(entitiesCount);
		
		List<String> entitiesList = new ArrayList<String>(entitiesCount.keySet());

		Registry registry = LocateRegistry.getRegistry("localhost");
		DBPediaServiceInterface dbPedia = (DBPediaServiceInterface) registry.lookup("DBPediaService");
		
		Tree tree = new Tree();
		
		for (int index = 0; index < entitiesList.size(); index++) {
			String entity = entitiesList.get(index);
			Term hierarchy = dbPedia.findTree(entity, LEVELS);
			for (int innerIndex = 0; innerIndex < entitiesList.size(); innerIndex++) {
				String innerEntity = entitiesList.get(innerIndex);
				Term result = hierarchy.find(innerEntity, true);
				if (result != null) {
					addHierarchy(tree, entity, result);
				}
			}
		}
		
		return new ApproachResponse(tree, entitiesList);
		
	}

	private void addHierarchy(Tree tree, String sonLabel, Term fatherTerm) {
		while (fatherTerm.getParent() != null) {
			addToTree(tree, fatherTerm.getLabel(), fatherTerm.getParent().getLabel());
			fatherTerm = fatherTerm.getParent();
		}
		addToTree(tree, fatherTerm.getLabel(), sonLabel);
	}
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		new RestrictEntityHierachicCurriculumMatcher().writeTree();
	}

}
