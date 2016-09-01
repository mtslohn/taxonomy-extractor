package br.ufsc.egc.curriculumextractor.model.json.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;

import br.ufsc.egc.curriculumextractor.model.json.Node;
import br.ufsc.egc.curriculumextractor.model.json.Text;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.curriculumextractor.util.TreeWriter;

public class JsonNodeWriter {
	
	private static final String FILE_NAME_TEMPLATE = "results/%s-%s.json";
	
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
	
	private String createFileName(String strategyName) {
		SimpleDateFormat sdf = new SimpleDateFormat(TreeWriter.DATE_FORMAT);
		return String.format(FILE_NAME_TEMPLATE, strategyName,
				sdf.format(Calendar.getInstance().getTime()));
	}
	
	public String printJson(Tree tree) {
		Gson gson = new Gson();
		Node jsonTree = parse(tree);
		return gson.toJson(jsonTree);
	}
	
	public void writeTree(String strategyName, Tree tree) {
		File file = new File(createFileName(strategyName));

		file.getParentFile().mkdirs();

		FileWriter fileWriter = null;
		BufferedWriter buffWriter = null;
		try {
			
			fileWriter = new FileWriter(file);
			buffWriter = new BufferedWriter(fileWriter);
			
			buffWriter.write(printJson(tree));
			
			buffWriter.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (buffWriter != null) {
				try {
					buffWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
