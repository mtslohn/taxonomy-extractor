package br.ufsc.egc.curriculumextractor.model.json.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

import br.ufsc.egc.curriculumextractor.model.json.Node;

public class JsonNodeReader {

	private static final String FILENAME = "results/json/curriculum/20000 lines - 500 entityThreshold - 1 levels-2016-09-14.19.27.49.json";

	public Node readJson(String json) {
		Gson gson = new Gson();
		Node node = gson.fromJson(json, Node.class);
		return node;
	}

	public void listNodes(Node node) {
		List<String> nodes = new LinkedList<>();
		addToList(nodes, node);
		Collections.sort(nodes);
		System.out.print("List<String> tree = Arrays.asList(new String[]{");
		Iterator<String> itNodes = nodes.iterator();
		while (itNodes.hasNext()) {
			String nodeText = itNodes.next();
			System.out.print("\"" + nodeText + "\"");
			if (itNodes.hasNext()) {
				System.out.print(", ");
			}
		}
		System.out.print("});");
	}

	private void addToList(List<String> nodes, Node node) {
		nodes.add(node.getText().getName());
		if (node.getChildren() != null) {
			for (Node child : node.getChildren()) {
				addToList(nodes, child);
			}
		}
	}

	public static void main(String[] args) throws IOException {

		JsonNodeReader thisReader = new JsonNodeReader();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(FILENAME));
			String json = bufferedReader.readLine();
			Node node = thisReader.readJson(json);
			thisReader.listNodes(node);
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		
		

	}

}
