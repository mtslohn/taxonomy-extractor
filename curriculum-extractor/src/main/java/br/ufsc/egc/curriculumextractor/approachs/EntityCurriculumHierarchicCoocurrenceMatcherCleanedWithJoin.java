package br.ufsc.egc.curriculumextractor.approachs;

import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;

public class EntityCurriculumHierarchicCoocurrenceMatcherCleanedWithJoin {

	public void process() {
		
		EntityCurriculumHierarchicCoocurrenceMatcher approach = new EntityCurriculumHierarchicCoocurrenceMatcher();
		
		ApproachResponse response = approach.createTree();
		Tree tree = response.getTree();

		tree.join();
		tree = tree.clean(response.getDiscoveredEntities());
		
		TreeWriter treeWriter = new TreeWriter();
		treeWriter.write(getClass().getSimpleName(), tree);
		
	}
	
	public static void main(String[] args) {
		new EntityCurriculumHierarchicCoocurrenceMatcherCleanedWithJoin().process();
	}

}
