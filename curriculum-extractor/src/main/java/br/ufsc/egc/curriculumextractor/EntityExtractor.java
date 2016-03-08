package br.ufsc.egc.curriculumextractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/**
 * Hello world!
 *
 */
public class EntityExtractor {

	private static final String NER_MODEL = "corpus/pt-ner.bin";

	public static void main(String[] args) {

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
						System.out.println(span.toString());
						System.out.println(result);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static boolean isValidString(String result) {
		if (result.trim().equals("PREP"))
			return false;
		return true;
	}

	private static String clearString(String dirtyText) {
		return dirtyText.replaceAll("\\.", ".").replaceAll("\n", " ");
	}

	public static TokenSpan getEntities() throws IOException {

		BufferedReader reader = null;
		try {
			TokenNameFinderModel model = new TokenNameFinderModel(new File(
					NER_MODEL));
			NameFinderME nameFinderME = new NameFinderME(model);
			reader = new BufferedReader(new FileReader(
					"src/main/resources/curriculum.list.txt"));
			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				line = line.replaceAll("\\(|\\)|\\,|\\.|\\;", "");
				builder.append(line);
				builder.append(" ");
				line = reader.readLine();
			}

			TokenSpan sentenceSpan = new TokenSpan();
			sentenceSpan.tokens = clearString(builder.toString()).split(" ");
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

	public static class TokenSpan {

		String[] tokens;
		Span[] spans;
		
	}

}
