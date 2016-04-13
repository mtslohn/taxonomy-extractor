package br.ufsc.egc.curriculumextractor.approachs;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;

public class EntityCurriculumCoocurrenceMatcherWithJoin extends
		AbstractEntityCurriculumMatcher {

	public void process() {
		
		EntityCurriculumCoocurrenceMatcher approach = new EntityCurriculumCoocurrenceMatcher();
		
		Tree tree = approach.createTree();
		
		tree.join();
		
		TreeWriter treeWriter = new TreeWriter();
		treeWriter.write(getClass().getSimpleName(), tree);
		
	}
	
	public static void main(String[] args) {
		new EntityCurriculumCoocurrenceMatcherWithJoin().process();
	}

}
