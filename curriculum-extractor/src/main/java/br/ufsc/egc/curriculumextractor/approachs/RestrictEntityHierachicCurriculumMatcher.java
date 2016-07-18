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
import br.ufsc.egc.curriculumextractor.model.TokenStatistics;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.NERMetrics;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;
import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface;
import br.ufsc.egc.dbpedia.reader.service.impl.DBPediaServiceImpl;

public class RestrictEntityHierachicCurriculumMatcher extends AbstractEntityCurriculumMatcher
		implements HierarchicApproach {

	private static final int DEFAULT_LEVELS = 3;
	private static final int DEFAULT_ENTITY_THRESHOLD = 1;

	private int levels;
	private int entityThreshold;
	private boolean useRMI = false;

	public RestrictEntityHierachicCurriculumMatcher() {
		this(DEFAULT_LEVELS, DEFAULT_ENTITY_THRESHOLD);
	}

	public RestrictEntityHierachicCurriculumMatcher(int levels, int entityThreshold) {
		setLevels(levels);
		setEntityThreshold(entityThreshold);
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}

	public void setEntityThreshold(int entityThreshold) {
		this.entityThreshold = entityThreshold;
	}

	@Override
	public int getLevels() {
		return levels;
	}

	public int getEntityThreshold() {
		return entityThreshold;
	}
	
	public ApproachResponse createTree() throws RemoteException, NotBoundException {
		EntityImprover improver = new EntityImprover();
		Map<String, Integer> entitiesCount = improver.getSortedEntitiesMap();

		entitiesCount = getFilteredMap(entitiesCount, getEntityThreshold());

		List<String> entities = new ArrayList<String>(entitiesCount.keySet());
		
		return createTree(entities, improver.getNumberOfTokens(), improver.getRecognizedTokens());
	}

	public ApproachResponse createTree(List<String> entities, int numberOfTokens, int recognizedTokens) throws RemoteException, NotBoundException {

		Tree tree = new Tree();

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

		// TODO fazer com que esse método multiplique também pelo total
		// de tokens encontrados no map de entidades
		TokenStatistics statistics = countUsedTokens(tree);
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
		new RestrictEntityHierachicCurriculumMatcher().writeTree();
	}

	@Override
	public void writeTree(List<String> entities, int numberOfTokens, int recognizedTokens) throws RemoteException, NotBoundException {
		ApproachResponse approachResponse = createTree(entities, numberOfTokens, recognizedTokens);
		Tree tree = approachResponse.getTree();
		
		TreeWriter treeWriter = new TreeWriter();
		String fileName = getClass().getSimpleName();
		if (this instanceof RestrictEntityHierachicCurriculumMatcher) {
			RestrictEntityHierachicCurriculumMatcher thisApproach = (RestrictEntityHierachicCurriculumMatcher) this;
			fileName = String.format("Frequencia absoluta - %s entityThreshold - %s levels", thisApproach.getEntityThreshold(), thisApproach.getLevels());
		}
		
		treeWriter.write(fileName, approachResponse.getNerMetrics(), approachResponse.getCyclicTokens(), tree);
	}

}
