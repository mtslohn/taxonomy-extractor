package br.ufsc.egc.curriculumextractor.approachs;

import java.rmi.RemoteException;

import br.ufsc.egc.curriculumextractor.approachs.selected.CurriculumCoocurrenceMatcher;
import br.ufsc.egc.curriculumextractor.model.ApproachResponse;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;

public class EntityCurriculumHierarchicCoocurrenceMatcherCleanedWithJoin implements HierarchicApproach {
	
	CurriculumCoocurrenceMatcher approach = new CurriculumCoocurrenceMatcher();
	
	public int getLevels() {
		return approach.getLevels(); 
	}

	public void process() throws RemoteException {
		
		ApproachResponse response = approach.createTree();
		Tree tree = response.getTree();

		tree.join();
		tree = tree.clean(response.getDiscoveredEntities());
		
		TreeWriter treeWriter = new TreeWriter();
		treeWriter.write(getClass().getSimpleName(), response.getNerMetrics(), response.getCyclicWords(), tree);
		
	}
	
	public static void main(String[] args) throws RemoteException {
		new EntityCurriculumHierarchicCoocurrenceMatcherCleanedWithJoin().process();
	}

}
