package br.ufsc.egc.curriculumextractor.approachs;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;

public abstract class AbstractEntityCurriculumMatcher {
	
	private static final Logger LOGGER = Logger.getLogger(AbstractEntityCurriculumMatcher.class);

	protected static Map<String, Integer> getFilteredMap(Map<String, Integer> entitiesCount) {
	
		LinkedHashMap<String, Integer> entitiesCountCleanMap = new LinkedHashMap<String, Integer>();
	
		for (String key : entitiesCount.keySet()) {
			if (entitiesCount.get(key) == 1) {
				break;
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
			LOGGER.warn("Tentativa de inserir pai e filhos iguais. Abortando...");
			return;
		}
		Term term = tree.find(broader);
		if (term == null) {
			term = new Term();
			term.setLabel(broader);
			tree.addRoot(term);
		}
		Term sonTerm = new Term();
		sonTerm.setLabel(narrower);
		term.addSon(sonTerm);
	}

}