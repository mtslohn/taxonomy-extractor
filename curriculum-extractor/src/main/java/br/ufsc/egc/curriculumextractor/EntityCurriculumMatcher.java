package br.ufsc.egc.curriculumextractor;

import java.util.LinkedHashMap;
import java.util.Map;

public class EntityCurriculumMatcher {
	
	public static void main(String[] args) {
		
		EntityImprover improver = new EntityImprover();
		Map<String, Integer> entitiesCount = improver.getSortedEntitiesMap();
		
		entitiesCount = getFilteredMap(entitiesCount);
		
		for (String key: entitiesCount.keySet()) {
			System.out.println(key + ": " + entitiesCount.get(key));
		}
		
		System.out.println(entitiesCount.size());
		
		CurriculumListReader curriculumListReader = new CurriculumListReader();
		Map<Integer, String> curriculumMap = curriculumListReader.read();
		
		
		
	}

	private static Map<String, Integer> getFilteredMap(
			Map<String, Integer> entitiesCount) {
		
		LinkedHashMap<String, Integer> entitiesCountCleanMap = new LinkedHashMap<String, Integer>();
		
		for (String key: entitiesCount.keySet()) {
			if (entitiesCount.get(key) == 1) {
				break;
			}
			entitiesCountCleanMap.put(key, entitiesCount.get(key));
		}
		
		return entitiesCountCleanMap;
		
	}

}
