package br.ufsc.egc.curriculumextractor.model;

import gnu.trove.map.hash.THashMap;

import java.util.Map;
import java.util.Set;

public class EntityPairCoocurrenceManager {
	
	private Map<EntityPair, Integer> pairsCoocurrence;
	
	public EntityPairCoocurrenceManager() {
		pairsCoocurrence = new THashMap<EntityPair, Integer>();
	}
	
	public void addPair(String entity1, String entity2) {
		if (!incrementIfExists(entity1, entity2) && !incrementIfExists(entity2, entity1)) {
			incrementNew(entity1, entity2);
		}
	}

	public boolean incrementIfExists(String entity1, String entity2) {
		EntityPair pair = new EntityPair(entity1, entity2);
		Integer cofrequency = pairsCoocurrence.get(pair);
		if (cofrequency != null) {
			cofrequency++;
			pairsCoocurrence.put(pair, cofrequency);
			return true;
		}
		return false;
	}
	
	public void incrementNew(String entity1, String entity2) {
		EntityPair pair = new EntityPair(entity1, entity2);
		pairsCoocurrence.put(pair, 1);
	}

	public Map<EntityPair, Integer> getPairsCoocurrence() {
		return pairsCoocurrence;
	}
	
}
