package br.ufsc.egc.curriculumextractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

/**
 * Hello world!
 *
 */
public class EntityExtractor {

	private static final String NER_MODEL = "corpus/pt-ner.bin";

	public static void main(String[] args) {

		try {
			SentenceSpan sentenceSpan = getEntities();

			for (Span span : sentenceSpan.spans) {
				if (!span.getType().equals("time")
						&& !span.getType().equals("numeric")) {

					StringBuilder spanBuilder = new StringBuilder();
					for (int index = span.getStart(); index < span.getEnd(); index++) {
						spanBuilder.append(sentenceSpan.sentences[index]);
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

	public static SentenceSpan getEntities() throws IOException {

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
				builder.append(line);
				builder.append(" ");
				line = reader.readLine();
			}

			SentenceSpan sentenceSpan = new SentenceSpan();
			sentenceSpan.sentences = clearString(builder.toString()).split(" ");
			sentenceSpan.spans = nameFinderME.find(sentenceSpan.sentences);
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

	private static class SentenceSpan {

		String[] sentences;
		Span[] spans;
	}
}
