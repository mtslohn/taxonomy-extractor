package br.ufsc.egc.curriculumextractor.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.CurriculumListReader;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/**
 * Hello world!
 *
 */
@Deprecated
public class EntityExtractor {

	private final static Logger LOGGER = Logger.getLogger(EntityExtractor.class);
	
	private static final String NER_MODEL_FILE = "corpus/pt-ner.bin";
	
	private int numberOfTokens;
	
	public int getNumberOfTokens() {
		return numberOfTokens;
	}

	public void process() {

		try {
			TokenSpan sentenceSpan = getEntities();

			for (Span span : sentenceSpan.spans) {
				if (!span.getType().equals("time")
						&& !span.getType().equals("numeric")) {

					StringBuilder spanBuilder = new StringBuilder();
					for (int index = span.getStart(); index < span.getEnd(); index++) {
						spanBuilder.append(sentenceSpan.tokens[index]);
						spanBuilder.append(" ");
					}

					String result = spanBuilder.toString();
					if (isValidString(result)) {
//						System.out.println(span.toString());
//						System.out.println(result);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
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

	public TokenSpan getEntities() throws IOException {

		BufferedReader reader = null;
		try {
			TokenNameFinderModel model = new TokenNameFinderModel(new File(
					NER_MODEL_FILE));
			NameFinderME nameFinderME = new NameFinderME(model);
			reader = new BufferedReader(new FileReader(CurriculumListReader.CURRICULUM_LIST_TXT));
			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();
			int lineReaded = 0;
			while (line != null && lineReaded < 200) {
				line = line.replaceAll("\\(|\\)|\\,|\\.|\\;", "");
				builder.append(line);
				builder.append(" ");
				line = reader.readLine();
				if (++lineReaded % 1000 == 0) {
					LOGGER.info(String.format("Linha de número %s lida", lineReaded));
				}
			}

			TokenSpan sentenceSpan = new TokenSpan();
			sentenceSpan.tokens = clearString(builder.toString()).split(" ");
			numberOfTokens = sentenceSpan.tokens.length;
			sentenceSpan.spans = nameFinderME.find(sentenceSpan.tokens);
			return sentenceSpan;
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
	
	public List<TokenSpan> getEntitiesReloaded() throws IOException {

		BufferedReader reader = null;
		try {
			TokenNameFinderModel model = new TokenNameFinderModel(new File(
					NER_MODEL_FILE));
			NameFinderME nameFinderME = new NameFinderME(model);
			reader = new BufferedReader(new FileReader(CurriculumListReader.CURRICULUM_LIST_TXT));
			
			List<TokenSpan> sentenceSpans = new ArrayList<TokenSpan>();
			
			String line = reader.readLine();
			
			int logIterator = 0;
			
			while (line != null) {
				logIterator++;
				line = line.replaceAll("\\(|\\)|\\,|\\.|\\;", "");
				TokenSpan sentenceSpan = new TokenSpan();
				sentenceSpan.tokens = clearString(line).split(" ");
				sentenceSpan.spans = nameFinderME.find(sentenceSpan.tokens);
				sentenceSpans.add(sentenceSpan);
				if (logIterator % 1000 == 0) {
					LOGGER.debug("Iterando sobre a instância de número " + logIterator);
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

	// desativado para não ser acionado por engano
	/*public static void main(String[] args) {
		new EntityExtractor().process();
	}*/

	public static class TokenSpan {

		public String[] tokens;
		public Span[] spans;

	}

}
