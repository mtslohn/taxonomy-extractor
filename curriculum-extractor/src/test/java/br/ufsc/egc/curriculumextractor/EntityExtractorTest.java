package br.ufsc.egc.curriculumextractor;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.core.EntityExtractor;
import br.ufsc.egc.curriculumextractor.core.EntityExtractor.TokenSpan;
import opennlp.tools.util.Span;

public class EntityExtractorTest {
	
	private final static Logger LOGGER = Logger.getLogger(EntityExtractorTest.class);

	public static void main(String[] args) {

		EntityExtractor entityExtractor = new EntityExtractor();

		try {
			List<TokenSpan> sentenceSpans = entityExtractor.getEntitiesReloaded();

			for (TokenSpan sentenceSpan : sentenceSpans) {
				for (Span span : sentenceSpan.spans) {
					if (!span.getType().equals("time") && !span.getType().equals("numeric")) {

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

}
