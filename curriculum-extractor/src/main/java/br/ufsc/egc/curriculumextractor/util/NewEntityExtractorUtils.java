package br.ufsc.egc.curriculumextractor.util;

import java.util.Set;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.hash.THashSet;

public class NewEntityExtractorUtils {
	
	public static Set<String> filterByEntityThreshold(TObjectIntMap<String> entitiesAndCount, int entityThreshold) {
		Set<String> entitiesSet = new THashSet<String>();
		for (String entity: entitiesAndCount.keySet()) {
			if (entitiesAndCount.get(entity) >= entityThreshold) {
				entitiesSet.add(entity);
			}
		}
		return entitiesSet;
	}

}
