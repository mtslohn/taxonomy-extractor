package br.ufsc.egc.curriculumextractor.service;

import br.ufsc.egc.curriculumextractor.batch.entities.EntityReader;
import br.ufsc.egc.curriculumextractor.core.NewEntityExtractor;
import br.ufsc.egc.curriculumextractor.model.entities.EntityRecognitionData;

public class EntityRecognitionServiceImpl implements EntityRecognitionService {

	@Override
	public EntityRecognitionData extractEntities(int readLinesLimit) {
		NewEntityExtractor nee = new NewEntityExtractor();
		return new EntityRecognitionData(
				nee.recognizeAndExtract(readLinesLimit),
				nee.getNumberOfTokens(),
				nee.getRecognizedTokens());
	}

	@Override
	public EntityRecognitionData getPreloadedEntities() {
		EntityReader entityReader = new EntityReader();
		entityReader.readEntities();
		return new EntityRecognitionData(
				entityReader.getEntitiesAndCount(),
				entityReader.getNumberOfTokens(),
				entityReader.getRecognizedTokens());
	}

}
