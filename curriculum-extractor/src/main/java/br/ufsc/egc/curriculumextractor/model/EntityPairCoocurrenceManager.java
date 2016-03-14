package br.ufsc.egc.curriculumextractor.model;

import java.util.HashSet;
import java.util.Set;

public class EntityPairCoocurrenceManager {
	
	private Set<EntityPairCoocurrence> pairs;
	
	public EntityPairCoocurrenceManager() {
		pairs = new HashSet<EntityPairCoocurrence>();
	}
	
	public void addPair(String entity1, String entity2) {
		EntityPairCoocurrence pair = findOrCreatePair(entity1, entity2);
		pair.increment();
	}

	private EntityPairCoocurrence findOrCreatePair(String entity1, String entity2) {
		EntityPairCoocurrence pair = findPair(entity1, entity2);
		if (pair == null) {
			pair = new EntityPairCoocurrence();
			pair.setEntity1(entity1);
			pair.setEntity2(entity2);
			pairs.add(pair);
		}
		return pair;
	}

	// substitui o equals para buscar por pares
	public EntityPairCoocurrence findPair(String entity1, String entity2) {
		for (EntityPairCoocurrence pair: pairs) {
			if (pair.getEntity1().equals(entity1) && pair.getEntity2().equals(entity2)) {
				return pair;
			}
			if (pair.getEntity1().equals(entity2) && pair.getEntity2().equals(entity1)) {
				return pair;
			}
		}
		return null;
	}
	
	public Set<EntityPairCoocurrence> getPairs() {
		return pairs;
	}

}
