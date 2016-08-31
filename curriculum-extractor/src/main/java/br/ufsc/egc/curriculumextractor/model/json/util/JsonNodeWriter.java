package br.ufsc.egc.curriculumextractor.model.json.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import br.ufsc.egc.curriculumextractor.model.json.Node;
import br.ufsc.egc.curriculumextractor.model.json.Text;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;

public class JsonNodeWriter {
	
	private static final String ROOT_NAME = "Taxonomia";

	public Node parse(Tree tree) {
		Node node = new Node();
		node.setText(new Text(ROOT_NAME));
		parse(node, tree.getRoots());
		return node;
	}
	
	private void parse(Node parent, List<Term> terms) {
		for (Term term: terms) {
			Node node = new Node();
			node.setText(new Text(term.getLabel()));
			parent.getChildren().add(node);
			if (!term.getSons().isEmpty()) {
				node.setChildren(new ArrayList<Node>());
				parse(node, term.getSons());
			}
		}
	}
	
	public String printJson(Tree tree) {
		Gson gson = new Gson();
		Node jsonTree = parse(tree);
		return gson.toJson(jsonTree);
	}

}
