package br.ufsc.egc.curriculumextractor.approachs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.TokenStatistics;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.procedure.TObjectProcedure;

public abstract class AbstractEntityCurriculumMatcher {
	
	private static final int ENTITY_THRESHOLD = 3;
	private static final Logger LOGGER = Logger.getLogger(AbstractEntityCurriculumMatcher.class);
	private static final String BLANKSPACE = " ";
	
	protected static Map<String, Integer> getFilteredMap(Map<String, Integer> entitiesCount) {
		return getFilteredMap(entitiesCount, ENTITY_THRESHOLD);
	}
	
	protected static Map<String, Integer> getFilteredMap(Map<String, Integer> entitiesCount, int entityThreshold) {
	
		LinkedHashMap<String, Integer> entitiesCountCleanMap = new LinkedHashMap<String, Integer>();
	
		for (String key : entitiesCount.keySet()) {
			if (entitiesCount.get(key) < entityThreshold) {
				continue;
			}
			entitiesCountCleanMap.put(key, entitiesCount.get(key));
		}
	
		return entitiesCountCleanMap;
	
	}

	public AbstractEntityCurriculumMatcher() {
		super();
	}
	
	public static void addToTree(Tree tree, String broader, String narrower) {
		if (broader.equalsIgnoreCase(narrower)) {
			LOGGER.debug("Tentativa de inserir pai e filhos iguais. Abortando...");
			return;
		}

		Term term = tree.find(broader);
		
		if (term == null) {
		
			term = new Term();
			term.setLabel(broader);
			tree.addRoot(term);
		
		} else {

			Term broaderParentIterator = term.getParent();
			
			while (broaderParentIterator != null) {
				if (broaderParentIterator.getLabel().equalsIgnoreCase(narrower)) {
					LOGGER.debug("Tentativa de inserir filho que já existe como pai. Abortando...");
					return;
				}
				broaderParentIterator = broaderParentIterator.getParent();
			}
			
		}
		
		Term sonTerm = new Term();
		sonTerm.setLabel(narrower);
		term.addSon(sonTerm);
	}
	
	public abstract ApproachResponse createTree() throws RemoteException, NotBoundException;
	
	public void writeTree() throws RemoteException, NotBoundException {
		
		throw new RuntimeException("Não suportado!!!");
		
	}
	
	protected TokenStatistics countUsedTokens(Tree tree, TObjectIntMap<String> entitiesAndCount) {
		TObjectIntHashMap<String> wordsSum = new TObjectIntHashMap<String>();
		TObjectIntHashMap<String> wordsCount = new TObjectIntHashMap<String>();
		for (Term root: tree.getRoots()) {
			createTokenStatistics(wordsSum, wordsCount, root, entitiesAndCount);
		}

		int tokenCount = 0;
		
		Set<String> cyclicWords = new HashSet<String>();

		for (Object word : wordsCount.keys()) {
//			tokenCount += ((String) word).split(BLANKSPACE).length;
			if (wordsCount.get(word) > 1) {
				cyclicWords.add((String) word);
			}
		}
		
		for (String entity : entitiesAndCount.keySet()) {
			int thisTermCount = entitiesAndCount.get(entity);
			tokenCount = entity.split(BLANKSPACE).length * thisTermCount;
			tokenCount += thisTermCount;
		}

		return new TokenStatistics(tokenCount, cyclicWords);

	}

	private void createTokenStatistics(TObjectIntHashMap<String> wordsSum, TObjectIntHashMap<String> wordsCount, Term thisTerm, TObjectIntMap<String> entitiesAndCount) {
		
		int factor = entitiesAndCount.get(thisTerm.getLabel());
		
		if (factor == 0) {
			
			FindCaseInsensitiveProcedure procedure = new FindCaseInsensitiveProcedure();
			procedure.setSearchKey(thisTerm.getLabel());
			entitiesAndCount.forEachKey(procedure);

			if (procedure.getFoundKey() != null) {
				factor = entitiesAndCount.get(procedure.getFoundKey());
			}
			
		}
		
		wordsSum.put(thisTerm.getLabel(), factor);
		wordsCount.adjustOrPutValue(thisTerm.getLabel(), 1, 1);
		
		for (Term son: thisTerm.getSons()) {
			createTokenStatistics(wordsSum, wordsCount, son, entitiesAndCount);
		}
	}
	
	private class FindCaseInsensitiveProcedure implements TObjectProcedure<String> {
		
		String searchKey = null;
		String foundKey = null;
		
		@Override
		public boolean execute(String key) {
			if (key.equalsIgnoreCase(searchKey)) {
				foundKey = key;
				return false;
			}
			return true;
		}

		public void setSearchKey(String searchKey) {
			this.searchKey = searchKey;
		}

		public String getFoundKey() {
			return foundKey;
		}
		
	}
	
}