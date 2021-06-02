package br.ufsc.egc.curriculumextractor.batch.entities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import br.ufsc.egc.curriculumextractor.core.NewEntityExtractor;
import br.ufsc.egc.curriculumextractor.util.constants.EntityExtractorConstants;
import gnu.trove.map.TObjectIntMap;

public class EntityExtractor {
	
	public static void main(String[] args) throws IOException {
		
		NewEntityExtractor nee = new NewEntityExtractor();
		TObjectIntMap<String> entitiesAndCount = nee.recognizeAndExtract(EntityExtractorConstants.READ_LINES_LIMIT);
		File file = new File(EntityExtractorConstants.FILENAME);
		if (file.exists()) {
			file.delete();
		}
		File folder = file.getParentFile();
		if (!folder.exists()) {
			folder.mkdir();
		}
		file.createNewFile();
		FileOutputStream fileStream = new FileOutputStream(file);
		ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
		objectStream.writeObject(entitiesAndCount);
		objectStream.writeInt(nee.getNumberOfTokens());
		objectStream.writeInt(nee.getRecognizedTokens());
		objectStream.close();
		fileStream.close();
		
	}

}
