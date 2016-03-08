package br.ufsc.egc.curriculumextractor;

import java.io.IOException;

import opennlp.tools.util.Span;

import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;

import br.ufsc.egc.curriculumextractor.EntityExtractor.SentenceSpan;

public class EntityImprover {
	
	public static void main(String[] args) {
		
		CharArraySet stopSet = PortugueseAnalyzer.getDefaultStopSet();
		
		try {
			SentenceSpan sentenceSpan = EntityExtractor.getEntities();

			for (Span span : sentenceSpan.spans) {
				if (!span.getType().equals("time")
						&& !span.getType().equals("numeric")) {

					StringBuilder spanBuilder = new StringBuilder();
					for (int index = span.getStart(); index < span.getEnd(); index++) {
						String term = sentenceSpan.sentences[index];
						if (index == span.getEnd() - 1) {
							if (stopSet.contains(term)) {
								continue; // nao concatena
							}
						} 
						spanBuilder.append(term);
						spanBuilder.append(" ");
					}

					String result = spanBuilder.toString();
					System.out.println(span.toString());
					System.out.println(result);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
