package br.ufsc.egc.curriculumextractor.approachs;

import java.rmi.RemoteException;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;

public class RestrictEntityHierachicCleanedCurriculumMatcherWithJoin implements HierarchicApproach {
	
	RestrictEntityHierachicCleanedCurriculumMatcher approach = new RestrictEntityHierachicCleanedCurriculumMatcher();
	
	public int getLevels() {
		return approach.getLevels();
	}

	public void process() throws RemoteException {
		
		RestrictEntityHierachicCleanedCurriculumMatcher approach = new RestrictEntityHierachicCleanedCurriculumMatcher();
		
		Tree tree = approach.createTree().getTree();
		
		tree.join();
		
		TreeWriter treeWriter = new TreeWriter();
		treeWriter.write(getClass().getSimpleName(), tree);
		
	}
	
	public static void main(String[] args) throws RemoteException {
		new RestrictEntityHierachicCleanedCurriculumMatcherWithJoin().process();
	}

}
