package br.ufsc.egc.curriculumextractor.service;

import br.ufsc.egc.curriculumextractor.model.entities.EntityRecognitionData;

public interface EntityRecognitionService {
	EntityRecognitionData extractEntities(int readLinesLimit);

	EntityRecognitionData getPreloadedEntities();
}
