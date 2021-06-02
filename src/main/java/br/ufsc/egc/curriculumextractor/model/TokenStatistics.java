package br.ufsc.egc.curriculumextractor.model;

import java.util.Set;

public class TokenStatistics {

	private int usedTokens;
	private Set<String> cyclicWords;

	public TokenStatistics(int usedTokens, Set<String> cyclicWords) {
		this.usedTokens = usedTokens;
		this.cyclicWords = cyclicWords;
	}

	public int getUsedTokens() {
		return usedTokens;
	}

	public void setUsedTokens(int tokenCount) {
		this.usedTokens = tokenCount;
	}

	public Set<String> getCyclicWords() {
		return cyclicWords;
	}

	public void setCyclicWords(Set<String> cyclicWords) {
		this.cyclicWords = cyclicWords;
	}

}
