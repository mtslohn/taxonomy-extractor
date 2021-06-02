package br.ufsc.egc.curriculumextractor.core;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import opennlp.tools.util.Span;

import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;

public class EntityImprover {

	Map<String, Integer> entitiesCount = new HashMap<String, Integer>();
	
	private int numberOfTokens;
	private int recognizedTokens;

	public Map<String, Integer> getSortedEntitiesMap() {

		// recupera o set de stop words padrao do Lucene
		CharArraySet stopSet = PortugueseAnalyzer.getDefaultStopSet();

		try {
			EntityExtractor.TokenSpan sentenceSpan = new EntityExtractor().getEntities();
			
			numberOfTokens = sentenceSpan.tokens.length;
			recognizedTokens = 0;

			for (Span span : sentenceSpan.spans) {
				if (!span.getType().equals("time")
						&& !span.getType().equals("numeric")) {

					StringBuilder spanBuilder = new StringBuilder();
					for (int index = span.getStart(); index < span.getEnd(); index++) {
						String term = sentenceSpan.tokens[index];
						if (index == span.getEnd() - 1) {
							if (stopSet.contains(term)) {
								continue; // nao concatena
							}
						}
						recognizedTokens++;
						spanBuilder.append(term);
						if (index < span.getEnd() - 1) {
							spanBuilder.append(" ");
						} 
					}

					String term = spanBuilder.toString();
					addToEntitiesCount(term);

				}
			}
			
			Map<String, Integer> sortedMap = sortByValue(entitiesCount);
			
			return sortedMap;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	private void addToEntitiesCount(String term) {

		if (entitiesCount.containsKey(term)) {
			int freq = entitiesCount.get(term);
			entitiesCount.put(term, freq + 1);
		} else {
			entitiesCount.put(term, 1);
		}

	}

	public <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	public int getNumberOfTokens() {
		return numberOfTokens;
	}
	
	public int getRecognizedTokens() {
		return recognizedTokens;
	}

}
