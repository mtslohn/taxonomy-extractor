package br.ufsc.egc.curriculumextractor.approachs;

import java.rmi.RemoteException;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;

public class EntityCurriculumCoocurrenceMatcherWithJoin {

	public void process() throws RemoteException {
		
		EntityCurriculumCoocurrenceMatcher approach = new EntityCurriculumCoocurrenceMatcher();
		
		Tree tree = approach.createTree().getTree();
		
		tree.join();
		
		TreeWriter treeWriter = new TreeWriter();
		treeWriter.write(getClass().getSimpleName(), tree);
		
	}
	
	public static void main(String[] args) throws RemoteException {
		new EntityCurriculumCoocurrenceMatcherWithJoin().process();
	}

}
