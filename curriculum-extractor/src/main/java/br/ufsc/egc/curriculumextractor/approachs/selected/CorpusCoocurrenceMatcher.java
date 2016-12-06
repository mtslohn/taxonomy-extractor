package br.ufsc.egc.curriculumextractor.approachs.selected;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufsc.egc.curriculumextractor.approachs.AbstractEntityCurriculumMatcher;
import br.ufsc.egc.curriculumextractor.approachs.HierarchicApproach;
import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.TokenStatistics;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.NERMetrics;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;
import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface;
import br.ufsc.egc.dbpedia.reader.service.impl.DBPediaServiceImpl;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

// Eh hierarquico
public class CorpusCoocurrenceMatcher extends AbstractEntityCurriculumMatcher {

	private static final int DEFAULT_LEVELS = 3;

	private int levels;
	private boolean useRMI = false;

	public CorpusCoocurrenceMatcher() {
		this(DEFAULT_LEVELS);
	}

	public CorpusCoocurrenceMatcher(int levels) {
		setLevels(levels);
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}

	public int getLevels() {
		return levels;
	}

	public ApproachResponse createTree() {
		throw new RuntimeException("Não suportado!!!");
	}

	public ApproachResponse createTree(TObjectIntMap<String> entitiesAndCount, int numberOfTokens, int recognizedTokens) throws RemoteException, NotBoundException {

		Tree tree = new Tree();
		
		List<String> entities = new ArrayList<String>(entitiesAndCount.keySet());

		Set<String> usedEntities = new HashSet<String>();

		DBPediaServiceInterface dbPedia = getDBPedia();

		for (int index = 0; index < entities.size(); index++) {
			String entity = entities.get(index);
			Term hierarchy = dbPedia.findTree(entity, getLevels());
			for (int innerIndex = 0; innerIndex < entities.size(); innerIndex++) {
				String innerEntity = entities.get(innerIndex);
				if (!entity.equalsIgnoreCase(innerEntity)) { // previne que se adicione a hierarquia do mesmo elemento
					Term result = hierarchy.find(innerEntity, true);
					if (result != null) {
						usedEntities.add(entity);
						usedEntities.add(innerEntity);
						addHierarchy(tree, entity, result);
					}
				}
			}
		}
		
		TObjectIntMap<String> usedEntitiesAndCount = new TObjectIntHashMap<String>();
		
		for (String usedEntity: usedEntities) {
			usedEntitiesAndCount.put(usedEntity, entitiesAndCount.get(usedEntity));
		}

		TokenStatistics statistics = countUsedTokens(tree, usedEntitiesAndCount);
		NERMetrics nerMetrics = new NERMetrics(numberOfTokens, recognizedTokens, statistics.getUsedTokens(), usedEntities);
		return new ApproachResponse(tree, entities, nerMetrics, statistics.getCyclicWords());
		
	}

	private DBPediaServiceInterface getDBPedia() throws RemoteException, NotBoundException {
		if (useRMI) {
			Registry registry = LocateRegistry.getRegistry("localhost");
			DBPediaServiceInterface dbPedia = (DBPediaServiceInterface) registry.lookup("DBPediaService");
			return dbPedia;
		} else {
			return DBPediaServiceImpl.getInstance();
		}
	}

	private void addHierarchy(Tree tree, String sonLabel, Term fatherTerm) {
		while (fatherTerm.getParent() != null) {
			addToTree(tree, fatherTerm.getLabel(), fatherTerm.getParent().getLabel());
			fatherTerm = fatherTerm.getParent();
		}
		addToTree(tree, fatherTerm.getLabel(), sonLabel);
	}

}
