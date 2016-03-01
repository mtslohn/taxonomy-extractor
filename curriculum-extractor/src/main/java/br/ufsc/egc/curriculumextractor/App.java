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
public class App {
	public static void main(String[] args) {

		BufferedReader reader = null;
		try {
			TokenNameFinderModel model = new TokenNameFinderModel(new File(
					"corpus/pt-ner.bin"));
			NameFinderME nameFinderME = new NameFinderME(model);
			reader = new BufferedReader(new FileReader(
					"src/main/resources/curriculum.clean.txt"));
			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				builder.append(line);
				builder.append(" ");
				line = reader.readLine();
			}
			String[] sentences = builder.toString().replaceAll("\\.", ".").split(" ");
			Span[] spans = nameFinderME.find(sentences);
			for (Span span : spans) {
				if (!span.getType().equals("time")
						&& !span.getType().equals("numeric")) {
					System.out.println(span.toString());
					StringBuilder spanBuilder = new StringBuilder();
					for (int index = span.getStart(); index < span.getEnd(); index++) {
						spanBuilder.append(sentences[index]);
						spanBuilder.append(" ");
					}
					System.out.println(spanBuilder.toString());
				}
			}
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
