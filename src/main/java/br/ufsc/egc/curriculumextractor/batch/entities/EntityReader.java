package br.ufsc.egc.curriculumextractor.batch.entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import br.ufsc.egc.curriculumextractor.util.constants.EntityExtractorConstants;
import gnu.trove.map.TObjectIntMap;

public class EntityReader {

	private TObjectIntMap<String> entitiesAndCount = null;
	private int numberOfTokens;
	private int recognizedTokens;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		EntityReader reader = new EntityReader();
		reader.readEntities();
		TObjectIntMap<String> recognizedEntities = reader.getEntitiesAndCount();
//		System.out.println(recognizedEntities);
		for (String key : recognizedEntities.keySet()) {
			if (key.contains("exat")) {
				System.out.println(key);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void readEntities() {
		File file = new File(EntityExtractorConstants.FILENAME);
		if (!file.exists()) {
			throw new RuntimeException("Arquivo de entidades não existe!");
		}

		FileInputStream fileStream = null;
		ObjectInputStream objectStream = null;
		try {
			fileStream = new FileInputStream(file);
			objectStream = new ObjectInputStream(fileStream);
			entitiesAndCount = (TObjectIntMap<String>) objectStream.readObject();
			numberOfTokens = objectStream.readInt();
			recognizedTokens = objectStream.readInt();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				objectStream.close();
				fileStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public TObjectIntMap<String> getEntitiesAndCount() {
		return entitiesAndCount;
	}

	public int getNumberOfTokens() {
		return numberOfTokens;
	}

	public int getRecognizedTokens() {
		return recognizedTokens;
	}

}
