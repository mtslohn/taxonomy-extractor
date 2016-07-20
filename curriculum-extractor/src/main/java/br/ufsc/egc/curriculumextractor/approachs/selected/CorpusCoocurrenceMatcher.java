package br.ufsc.egc.curriculumextractor.approachs.selected;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

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

// Eh hierarquico
public class CorpusCoocurrenceMatcher extends AbstractEntityCurriculumMatcher
		implements HierarchicApproach {

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

	@Override
	public int getLevels() {
		return levels;
	}

	public ApproachResponse createTree() {
		throw new RuntimeException("Não suportado!!!");
	}

	public ApproachResponse createTree(TObjectIntMap<String> entitiesAndCount, int numberOfTokens, int recognizedTokens) throws RemoteException, NotBoundException {

		Tree tree = new Tree();
		
		List<String> entities = new ArrayList<String>(entitiesAndCount.keySet());

		DBPediaServiceInterface dbPedia = getDBPedia();

		for (int index = 0; index < entities.size(); index++) {
			String entity = entities.get(index);
			Term hierarchy = dbPedia.findTree(entity, getLevels());
			for (int innerIndex = 0; innerIndex < entities.size(); innerIndex++) {
				String innerEntity = entities.get(innerIndex);
				Term result = hierarchy.find(innerEntity, true);
				if (result != null) {
					addHierarchy(tree, entity, result);
				}
			}
		}

		TokenStatistics statistics = countUsedTokens(tree, entitiesAndCount);
		NERMetrics nerMetrics = new NERMetrics(numberOfTokens, recognizedTokens, statistics.getUsedTokens());
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

	public static void main(String[] args) throws RemoteException, NotBoundException {
		new CorpusCoocurrenceMatcher().writeTree();
	}

	@Override
	public void writeTree(int entityThreshold, TObjectIntMap<String> entitiesAndCount, int numberOfTokens, int recognizedTokens) throws RemoteException, NotBoundException {
		ApproachResponse approachResponse = createTree(entitiesAndCount, numberOfTokens, recognizedTokens);
		Tree tree = approachResponse.getTree();
		TreeWriter treeWriter = new TreeWriter();
		String fileName = String.format("Corpus Coocurrence - %s entityThreshold - %s levels", entityThreshold, this.getLevels());
		treeWriter.write(fileName, approachResponse.getNerMetrics(), approachResponse.getCyclicTokens(), tree);
	}

}
