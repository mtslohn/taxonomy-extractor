package br.ufsc.egc.curriculumextractor.approachs;

import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;

public class EntityCurriculumHierarchicCoocurrenceMatcherCleanedWithJoin implements HierarchicApproach {
	
	EntityCurriculumHierarchicCoocurrenceMatcher approach = new EntityCurriculumHierarchicCoocurrenceMatcher();
	
	public int getLevels() {
		return approach.getLevels(); 
	}

	public void process() {
		
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
