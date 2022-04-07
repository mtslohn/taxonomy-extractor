package br.ufsc.egc.curriculumextractor.batch;

import br.ufsc.egc.curriculumextractor.model.entities.EntityRecognitionData;
import br.ufsc.egc.curriculumextractor.service.EntityRecognitionService;
import br.ufsc.egc.curriculumextractor.service.EntityRecognitionServiceImpl;

class EntityRecognitionHelper {

	static EntityRecognitionData loadEntities(boolean extractOnRuntime, int readLinesLimit) {
		EntityRecognitionService entityRecognitionService = new EntityRecognitionServiceImpl();
		if (extractOnRuntime) {
			return entityRecognitionService.extractEntities(readLinesLimit);
		} else {
			return entityRecognitionService.getPreloadedEntities();
		}
	}
}
