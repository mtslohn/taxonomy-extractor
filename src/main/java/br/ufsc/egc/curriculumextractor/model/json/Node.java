package br.ufsc.egc.curriculumextractor.model.json;

import java.util.List;

public class Node {

	private Text text;
	private List<Node> children;

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

}
