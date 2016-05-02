package br.ufsc.egc.curriculumextractor.approachs;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;

public class RestrictEntityHierachicCleanedCurriculumMatcherWithJoin {

	public void process() {
		
		RestrictEntityHierachicCleanedCurriculumMatcher approach = new RestrictEntityHierachicCleanedCurriculumMatcher();
		
		Tree tree = approach.createTree();
		
		tree.join();
		
		TreeWriter treeWriter = new TreeWriter();
		treeWriter.write(getClass().getSimpleName(), tree);
		
	}
	
	public static void main(String[] args) {
		new RestrictEntityHierachicCleanedCurriculumMatcherWithJoin().process();
	}

}
