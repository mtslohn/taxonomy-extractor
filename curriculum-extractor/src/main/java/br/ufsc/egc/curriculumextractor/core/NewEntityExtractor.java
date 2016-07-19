package br.ufsc.egc.curriculumextractor.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;

import br.ufsc.egc.curriculumextractor.CurriculumListReader;
import gnu.trove.map.hash.TObjectIntHashMap;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/**
 * Hello world!
 *
 */
public class NewEntityExtractor {
	
	private static final Logger LOGGER = Logger.getLogger(NewEntityExtractor.class);

	private static final String ENTITY_TYPE_NUMERIC = "numeric";
	private static final String ENTITY_TYPE_TIME = "time";

	private static final String NER_MODEL_FILE = "corpus/pt-ner.bin";
	
	private String nerModelPath;
	private String sourcePath;

	private int numberOfTokens;
	private int recognizedTokens;
	
	public NewEntityExtractor() {
		this(NER_MODEL_FILE, CurriculumListReader.CURRICULUM_LIST_TXT);
	}
	
	public NewEntityExtractor(String nerModelPath, String sourcePath) {
		this.nerModelPath = nerModelPath;
		this.sourcePath = sourcePath;
		this.numberOfTokens = 0;
	}
	
	
	public int getNumberOfTokens() {
		return numberOfTokens;
	}
	
	public int getRecognizedTokens() {
		return recognizedTokens;
	}
	
	public TObjectIntHashMap<String> recognizeAndExtract() {
		return recognizeAndExtract(-1);
	}

	public TObjectIntHashMap<String> recognizeAndExtract(int lineLimit) {

		// recupera o set de stop words padrao do Lucene
		CharArraySet stopSet = PortugueseAnalyzer.getDefaultStopSet();

		TObjectIntHashMap<String> entitiesCountMap = new TObjectIntHashMap<String>();
		
		try {
			
			List<SentenceSpan> sentenceSpans = recognizeSpans(lineLimit);
			
			numberOfTokens = 0;
			recognizedTokens = 0;

			for (SentenceSpan sentenceSpan: sentenceSpans) {
				
				numberOfTokens += sentenceSpan.tokens.length;
				
				for (Span span : sentenceSpan.spans) {
					if (!span.getType().equals(ENTITY_TYPE_TIME)
							&& !span.getType().equals(ENTITY_TYPE_NUMERIC)) {
	
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

						String result = spanBuilder.toString();
						if (isValidString(result)) {
							entitiesCountMap.adjustOrPutValue(result, 1, 1);
						}
					}
				}
				
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return entitiesCountMap;
	}

	public List<SentenceSpan> recognizeSpans(int lineLimit) throws IOException {

		BufferedReader reader = null;
		try {
			
			TokenNameFinderModel model = new TokenNameFinderModel(new File(nerModelPath));
			NameFinderME nameFinderME = new NameFinderME(model);
			
			reader = new BufferedReader(new FileReader(sourcePath));
			
			List<SentenceSpan> sentenceSpans = new ArrayList<NewEntityExtractor.SentenceSpan>();
			
			int readLines = 0;
			
			String line = reader.readLine();
			
			while (line != null && (lineLimit < 0 || readLines < lineLimit)) {
				line = line.replaceAll("\\(|\\)|\\,|\\.|\\;", "");
				SentenceSpan sentenceSpan = new SentenceSpan();
				sentenceSpan.tokens = clearString(line).split(" ");
				numberOfTokens += sentenceSpan.tokens.length;
				sentenceSpan.spans = nameFinderME.find(sentenceSpan.tokens);
				
				sentenceSpans.add(sentenceSpan);
				readLines++;
				if (readLines % 100 == 0) { 
					LOGGER.debug("Linhas lidas: " + readLines);
				}
				
				line = reader.readLine();
			}
			
			return sentenceSpans;

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private boolean isValidString(String result) {
		if (result.trim().equals("PREP"))
			return false;
		return true;
	}

	private String clearString(String dirtyText) {
		return dirtyText.replaceAll("\\.", ".").replaceAll("\n", " ");
	}

	public static class SentenceSpan {

		String[] tokens;
		Span[] spans;

	}

}
