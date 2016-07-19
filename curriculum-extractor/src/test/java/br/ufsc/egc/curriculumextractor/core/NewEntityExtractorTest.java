package br.ufsc.egc.curriculumextractor.core;

import org.junit.Test;

import gnu.trove.map.hash.TObjectIntHashMap;

public class NewEntityExtractorTest {
	
	@Test
	public void outputTest() {
		NewEntityExtractor nee = new NewEntityExtractor();
		TObjectIntHashMap<String> result = nee.recognizeAndExtract();
		for (String resultKey: result.keySet()) {
			System.out.println(resultKey + " : " + result.get(resultKey));
		}
	}
	
	@Test
	public void outputTestFreqGreaterThan() {
		NewEntityExtractor nee = new NewEntityExtractor();
		TObjectIntHashMap<String> result = nee.recognizeAndExtract(3000);
		for (String resultKey: result.keySet()) {
			if (result.get(resultKey) > 100) {
				System.out.println(resultKey + " : " + result.get(resultKey));
			}
		}
	}

}
