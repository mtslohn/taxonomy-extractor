package br.ufsc.egc.curriculumextractor.model;

import java.util.Map;

import gnu.trove.map.hash.THashMap;

public class EntityPairCoocurrenceManager {
	
	private Map<String, EntityPair> pairsIndex;
	private Map<EntityPair, Integer> pairsCoocurrence;
	
	public EntityPairCoocurrenceManager() {
		pairsIndex = new THashMap<String, EntityPair>();
		pairsCoocurrence = new THashMap<EntityPair, Integer>();
	}
	
	public void addPair(String entity1, String entity2) {
		if (!incrementIfExists(entity1, entity2) && !incrementIfExists(entity2, entity1)) {
			incrementNew(entity1, entity2);
		}
	}

	public boolean incrementIfExists(String entity1, String entity2) {
		
		EntityPair pair;
		
		String key1 = entity1.toLowerCase() + entity2.toLowerCase();
		pair = pairsIndex.get(key1);
		
		String key2 = entity2.toLowerCase() + entity1.toLowerCase();
		pair = pairsIndex.get(key2);
		
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
		
		String key = entity1.toLowerCase() + entity2.toLowerCase();
		pairsIndex.put(key, pair);
		
		pairsCoocurrence.put(pair, 1);
	}

	public Map<EntityPair, Integer> getPairsCoocurrence() {
		return pairsCoocurrence;
	}
	
}
