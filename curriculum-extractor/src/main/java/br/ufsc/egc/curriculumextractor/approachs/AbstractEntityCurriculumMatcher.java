package br.ufsc.egc.curriculumextractor.approachs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;

public abstract class AbstractEntityCurriculumMatcher {
	
	private static final int ENTITY_THRESHOLD = 3;
	private static final Logger LOGGER = Logger.getLogger(AbstractEntityCurriculumMatcher.class);
	
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
		
		Tree tree = createTree().getTree();
		
		TreeWriter treeWriter = new TreeWriter();
		String fileName = getClass().getSimpleName();
		if (this instanceof RestrictEntityHierachicCurriculumMatcher) {
			RestrictEntityHierachicCurriculumMatcher thisApproach = (RestrictEntityHierachicCurriculumMatcher) this;
			fileName = String.format("Frequencia absoluta - %s entityThreshold - %s levels", thisApproach.getEntityThreshold(), thisApproach.getLevels());
		} else 		if (this instanceof EntityCurriculumHierarchicCoocurrenceMatcher) {
			EntityCurriculumHierarchicCoocurrenceMatcher thisApproach = (EntityCurriculumHierarchicCoocurrenceMatcher) this;
			fileName = String.format("Coocorrencia - %s entityThreshold - %s levels", thisApproach.getEntityThreshold(), thisApproach.getLevels());
		}		treeWriter.write(fileName, tree);
		
	}
	
}