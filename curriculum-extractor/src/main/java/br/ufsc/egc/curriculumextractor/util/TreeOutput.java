package br.ufsc.egc.curriculumextractor.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;


public class TreeOutput {

	public void write(Tree tree) {
		
		FileOutputStream fileOutput = null;
		ObjectOutputStream objectOutput = null;
		
		try {
			
			File file = new File(TreeJoiner.BINARY_INPUT_FILE);
			
			if (file.exists()) {
				file.delete();
			}
			
			file.getParentFile().mkdirs();
			file.createNewFile();
			
			fileOutput = new FileOutputStream(file);
			
			objectOutput = new ObjectOutputStream(fileOutput); 
			
			objectOutput.writeObject(tree);
			
			objectOutput.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (objectOutput != null) {
				try {
					objectOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutput != null) {
				try {
					fileOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
}
