package br.ufsc.egc.curriculumextractor.util;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

public class NewEntityExtractorUtils {
	
	public static TObjectIntMap<String> filterByEntityThreshold(TObjectIntMap<String> entitiesAndCount, int entityThreshold) {
		TObjectIntMap<String> filteredMap = new TObjectIntHashMap<String>();
		for (String entity: entitiesAndCount.keySet()) {
			int count = entitiesAndCount.get(entity);
			if (count >= entityThreshold) {
				filteredMap.put(entity, count);
			}
		}
		return filteredMap;
	}

}
