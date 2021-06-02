package br.ufsc.egc.curriculumextractor.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.approachs.AbstractEntityCurriculumMatcher;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;

public class TreeJoiner {
	
	private static final Logger LOGGER = Logger.getLogger(TreeJoiner.class);
	
	public static final String BINARY_INPUT_FILE = "results/tree.bin";

	public void process() {
		
		FileInputStream fileInput;
		try {
			fileInput = new FileInputStream(BINARY_INPUT_FILE);
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			Tree tree = (Tree) objectInput.readObject();
			
			tree.join();
			
			System.out.println(tree.print());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new TreeJoiner().process();
	}
	
}
