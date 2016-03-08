package br.ufsc.egc.curriculumextractor;

import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;

public class EntityImprover {
	
	public static void main(String[] args) {
		
		CharArraySet stopSet = PortugueseAnalyzer.getDefaultStopSet();
		System.out.println(stopSet.contains(""));
		
	}

}
