package br.ufsc.egc.curriculumextractor;

import java.io.File;
import java.io.IOException;

import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.InvalidFormatException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		
		try {
			TokenNameFinderModel model = new TokenNameFinderModel(new File("corpus/amazonia.per"));
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
