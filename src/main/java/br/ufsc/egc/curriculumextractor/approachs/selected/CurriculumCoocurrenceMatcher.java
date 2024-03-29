package br.ufsc.egc.curriculumextractor.approachs.selected;

import br.ufsc.egc.curriculumextractor.CurriculumListReader;
import br.ufsc.egc.curriculumextractor.approachs.AbstractEntityCurriculumMatcher;
import br.ufsc.egc.curriculumextractor.model.*;
import br.ufsc.egc.curriculumextractor.model.entities.ApproachEntityRecognitionMetrics;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.dbpedia.reader.service.DBPediaService;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.apache.log4j.Logger;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

// Eh hierarquico
public class CurriculumCoocurrenceMatcher extends AbstractEntityCurriculumMatcher {

	private static final int DEFAULT_LEVELS = 1;

	private static final Logger LOGGER = Logger.getLogger(CurriculumCoocurrenceMatcher.class);

	private int levels;
	private Map<Integer, String> curriculumMap;
	private EntityPairCoocurrenceManager coocurrenceManager;
	private List<String> entities;
	private TObjectIntMap<String> entitiesAndCount;
	private DBPediaService dbPediaService;

	public CurriculumCoocurrenceMatcher(int lineLimit) throws IOException {
		setLevels(DEFAULT_LEVELS);
		CurriculumListReader curriculumListReader = new CurriculumListReader();
		curriculumMap = curriculumListReader.read(lineLimit);
		dbPediaService = getDBPediaService();
	}

	public int getLevels() {
		return levels;
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}

	public void prepareForEntities(TObjectIntMap<String> entitiesAndCount) {

		this.entitiesAndCount = entitiesAndCount;

		List<String> entities = new ArrayList<String>(entitiesAndCount.keySet());
		this.entities = entities;

		File correlationsFile;
		try {

			correlationsFile = File.createTempFile("correlations", String.valueOf(Math.random() * 100000000));
			correlationsFile.deleteOnExit();

			FileOutputStream fos = new FileOutputStream(correlationsFile);
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(gzos);

			for (Integer curriculumKey : curriculumMap.keySet()) {
				if (curriculumKey % 100 == 0) {
					LOGGER.info("Identificando correlações para o currículo de número " + (curriculumKey + 1) + "/"
							+ curriculumMap.size());
				}
				String curriculumText = curriculumMap.get(curriculumKey);

				CurriculumCorrelation correlation = new CurriculumCorrelation();
				correlation.setCurriculumId(curriculumKey);

				for (int indexOuter = 0; indexOuter < entities.size(); indexOuter++) {
					String entityOuter = entities.get(indexOuter);
					// indexOf eh utilizado em vez de contains pela performance
					if (curriculumText.indexOf(entityOuter) != -1) {
						for (int indexInner = indexOuter + 1; indexInner < entities.size(); indexInner++) {
							String entityInner = entities.get(indexInner);
							if (curriculumText.indexOf(entityInner) != -1) {
								EntityPair pair = new EntityPair();
								pair.setEntity1(entityOuter);
								pair.setEntity2(entityInner);
								correlation.getPairs().add(pair);
							}
						}
					}
				}

				if (!correlation.getPairs().isEmpty()) {
					correlation.writeExternal(oos);
				}

			}

			coocurrenceManager = new EntityPairCoocurrenceManager();

			oos.flush();
			gzos.flush();
			fos.flush();

			oos.close();
			gzos.close();
			fos.close();

			int index = 0;

			FileInputStream fis = new FileInputStream(correlationsFile);
			GZIPInputStream gzis = new GZIPInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(gzis);

			boolean eof = false;

			while (!eof) {
				if (index % 1000 == 0) {
					LOGGER.info("Processando correlações para o par " + index);
				}
				CurriculumCorrelation correlation = new CurriculumCorrelation();

				try {
					correlation.readExternal(ois);
					for (EntityPair pair : correlation.getPairs()) {
						coocurrenceManager.addPair(pair.getEntity1(), pair.getEntity2());
					}
					index++;
				} catch (EOFException e) {
					eof = true;
				}
			}

			ois.close();
			gzis.close();
			fis.close();

			correlationsFile.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ApproachResponse createTree(int numberOfTokens, int recognizedTokens, int minimumCoocurrence)
			throws RemoteException {

		// ja tenho as relacoes e a frequencia delas... agora eh hora de montar
		// a arvore
		// TODO colocar na tree as relacoes descobertas e validadas na DBPedia

		Tree tree = new Tree();

		List<EntityPair> keyList = new ArrayList<EntityPair>(coocurrenceManager.getPairsCoocurrence().keySet());

		Set<String> usedEntities = new HashSet<String>();

		for (int index = 0; index < keyList.size(); index++) {
			if (index % 1000 == 0) {
				LOGGER.info("Procurando hierarquias para o par " + index + "/" + keyList.size());
			}
			EntityPair pair = keyList.get(index);
			LOGGER.debug("Coocurrence: " + coocurrenceManager.getPairsCoocurrence().get(pair));
			if (!pair.getEntity1().equalsIgnoreCase(pair.getEntity2())
					&& coocurrenceManager.getPairsCoocurrence().get(pair) >= minimumCoocurrence) {
				findAndAddHierarchy(tree, pair.getEntity1(), pair.getEntity2(), usedEntities);
				findAndAddHierarchy(tree, pair.getEntity2(), pair.getEntity1(), usedEntities);
			}
		}

		TObjectIntMap<String> usedEntitiesAndCount = new TObjectIntHashMap<String>();

		for (String usedEntity : usedEntities) {
			usedEntitiesAndCount.put(usedEntity, entitiesAndCount.get(usedEntity));
		}

		TokenStatistics statistics = countUsedTokens(tree, usedEntitiesAndCount);
		ApproachEntityRecognitionMetrics approachNerMetrics = new ApproachEntityRecognitionMetrics(numberOfTokens, recognizedTokens, statistics.getUsedTokens(),
				usedEntities);
		return new ApproachResponse(tree, entities, approachNerMetrics, statistics.getCyclicWords());

	}

	private void findAndAddHierarchy(Tree tree, String sonLabel,
	                                 String fatherLabel, Set<String> usedEntities) throws RemoteException {
		Term hierarchy = dbPediaService.findTree(sonLabel, getLevels());
		if (hierarchy != null) {
			Term result = hierarchy.find(fatherLabel, true);
			if (result != null) {
				usedEntities.add(sonLabel);
				usedEntities.add(fatherLabel);
				addHierarchy(tree, sonLabel, result);
			}
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
