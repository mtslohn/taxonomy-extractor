package br.ufsc.egc.curriculumextractor.approachs.selected;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.CurriculumListReader;
import br.ufsc.egc.curriculumextractor.approachs.AbstractEntityCurriculumMatcher;
import br.ufsc.egc.curriculumextractor.approachs.HierarchicApproach;
import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.CurriculumCorrelation;
import br.ufsc.egc.curriculumextractor.model.EntityPair;
import br.ufsc.egc.curriculumextractor.model.EntityPairCoocurrenceManager;
import br.ufsc.egc.curriculumextractor.model.TokenStatistics;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.NERMetrics;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;
import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface;
import br.ufsc.egc.dbpedia.reader.service.impl.DBPediaServiceImpl;
import gnu.trove.map.TObjectIntMap;

// Eh hierarquico
public class CurriculumCoocurrenceMatcher extends
		AbstractEntityCurriculumMatcher implements HierarchicApproach {

	private static final int DEFAULT_LEVELS = 1;

	private static final Logger LOGGER = Logger
			.getLogger(CurriculumCoocurrenceMatcher.class);

	private int levels;
	private Map<Integer, String> curriculumMap;

	private EntityPairCoocurrenceManager coocurrenceManager;

	private List<String> entities;

	private TObjectIntMap<String> entitiesAndCount;

	public CurriculumCoocurrenceMatcher(int lineLimit) {
		setLevels(DEFAULT_LEVELS);
		CurriculumListReader curriculumListReader = new CurriculumListReader();
		curriculumMap = curriculumListReader.read(lineLimit);
	}

	public int getLevels() {
		return levels;
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}
	public ApproachResponse createTree() {
		throw new RuntimeException("Não suportado!!!");
	}
	
	public void prepareForEntities(TObjectIntMap<String> entitiesAndCount) {
		
		this.entitiesAndCount = entitiesAndCount;

		List<String> entities = new ArrayList<String>(entitiesAndCount.keySet());
		this.entities = entities;

		List<CurriculumCorrelation> correlations = new ArrayList<CurriculumCorrelation>();

		for (Integer curriculumKey : curriculumMap.keySet()) {
			if (curriculumKey % 100 == 0) {
				LOGGER.info("Identificando correlações para o currículo de número " + (curriculumKey + 1) + "/" + curriculumMap.size());
			}
			String curriculum = curriculumMap.get(curriculumKey);

			CurriculumCorrelation correlation = new CurriculumCorrelation();
			correlation.setCurriculumId(curriculumKey);
			
			for (int indexOuter = 0; indexOuter < entities.size(); indexOuter++) {
				String entityOuter = entities.get(indexOuter);
				if (curriculum.contains(entityOuter)) {
					for (int indexInner = indexOuter + 1; indexInner < entities
							.size(); indexInner++) {
						String entityInner = entities.get(indexInner);
						if (curriculum.contains(entityInner)) {
							EntityPair pair = new EntityPair();
							pair.setEntity1(entityOuter);
							pair.setEntity2(entityInner);
							correlation.getPairs().add(pair);
						}
					}
				}
			}

			if (!correlation.getPairs().isEmpty()) {
				correlations.add(correlation);
			}
			
		}

		coocurrenceManager = new EntityPairCoocurrenceManager();

		for (int index = 0; index < correlations.size(); index++) {
			if (index % 1000 == 0) {
				LOGGER.info("Processando correlações para o par " + index);
			}
			CurriculumCorrelation correlation = correlations.get(index);
			for (EntityPair pair : correlation.getPairs()) {
				coocurrenceManager.addPair(pair.getEntity1(), pair.getEntity2());
			}
		}
		
	}

	public ApproachResponse createTree(int numberOfTokens, int recognizedTokens) throws RemoteException, NotBoundException {

		// ja tenho as relacoes e a frequencia delas... agora eh hora de montar
		// a arvore
		// TODO colocar na tree as relacoes descobertas e validadas na DBPedia

		DBPediaServiceInterface dbPediaService = DBPediaServiceImpl
				.getInstance();

		Tree tree = new Tree();

		List<EntityPair> keyList = new ArrayList<EntityPair>(coocurrenceManager
				.getPairsCoocurrence().keySet());

		for (int index = 0; index < keyList.size(); index++) {
			if (index % 1000 == 0) {
				LOGGER.info("Procurando hierarquias para o par " + index + "/"
						+ keyList.size());
			}
			EntityPair pair = keyList.get(index);
			findAndAddHierarchy(dbPediaService, tree, pair.getEntity1(),
					pair.getEntity2());
			findAndAddHierarchy(dbPediaService, tree, pair.getEntity2(),
					pair.getEntity1());
		}

		TokenStatistics statistics = countUsedTokens(tree, entitiesAndCount);
		NERMetrics nerMetrics = new NERMetrics(numberOfTokens, recognizedTokens, statistics.getUsedTokens());
		return new ApproachResponse(tree, entities, nerMetrics, statistics.getCyclicWords());

	}

	private void findAndAddHierarchy(DBPediaServiceInterface dbPediaService,
			Tree tree, String sonLabel, String fatherLabel)
			throws RemoteException {
		Term hierarchy = dbPediaService.findTree(sonLabel, getLevels());
		if (hierarchy != null) {
			Term result = hierarchy.find(fatherLabel, true);
			if (result != null) {
				addHierarchy(tree, sonLabel, result);
			}
		}
	}

	private void addHierarchy(Tree tree, String sonLabel, Term fatherTerm) {
		while (fatherTerm.getParent() != null) {
			addToTree(tree, fatherTerm.getLabel(), fatherTerm.getParent()
					.getLabel());
			fatherTerm = fatherTerm.getParent();
		}
		addToTree(tree, fatherTerm.getLabel(), sonLabel);
	}

	@Override
	public void writeTree(int entityThreshold, TObjectIntMap<String> entitiesAndCount, int numberOfTokens, int recognizedTokens) throws RemoteException, NotBoundException {
		ApproachResponse approachResponse = createTree(numberOfTokens, recognizedTokens);
		Tree tree = approachResponse.getTree();
		TreeWriter treeWriter = new TreeWriter();
		String fileName = String.format("Curriculum Coocurrence - %s entityThreshold - %s levels", entityThreshold, this.getLevels());
		treeWriter.write(fileName, approachResponse.getNerMetrics(), approachResponse.getCyclicWords(), tree);
	}

}
