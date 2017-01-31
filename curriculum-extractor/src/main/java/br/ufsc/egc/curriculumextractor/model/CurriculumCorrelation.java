package br.ufsc.egc.curriculumextractor.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CurriculumCorrelation {

	private int curriculumId;
	private List<EntityPair> pairs = new ArrayList<EntityPair>();

	public int getCurriculumId() {
		return curriculumId;
	}

	public void setCurriculumId(int curriculumId) {
		this.curriculumId = curriculumId;
	}

	public List<EntityPair> getPairs() {
		return pairs;
	}

	public void setPairs(List<EntityPair> pairs) {
		this.pairs = pairs;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("CurriculumCorrelation [curriculumId=" + curriculumId
				+ ", pairs="); 
		Iterator<EntityPair> itPairs = pairs.iterator();
		while (itPairs.hasNext()) {
			EntityPair pair = itPairs.next();
			output.append(pair);
			if (itPairs.hasNext()) {
				output.append(", ");
			}
		}
		output.append("]");
		return output.toString();
	}

	public void writeExternal(RandomAccessFile raf) throws IOException {
		raf.writeInt(curriculumId);
		raf.writeInt(pairs.size());
		for (EntityPair pair : pairs) {
			raf.writeUTF(pair.getEntity1());
			raf.writeUTF(pair.getEntity2());
		}
	}
	
	public void writeExternal(ObjectOutputStream out) throws IOException {
		out.writeInt(curriculumId);
		out.writeInt(pairs.size());
		for (EntityPair pair : pairs) {
			out.writeUTF(pair.getEntity1());
			out.writeUTF(pair.getEntity2());
		}
	}
	
	public void readExternal(RandomAccessFile raf) throws IOException {
		curriculumId = raf.readInt();
		int pairsAmount = raf.readInt();
		for (int pairNumber = 0; pairNumber < pairsAmount; pairNumber++) {
			EntityPair pair = new EntityPair();
			pair.setEntity1(raf.readUTF());
			pair.setEntity2(raf.readUTF());
			pairs.add(pair);
		}
	}
	
	public void readExternal(ObjectInputStream in) throws IOException {
		curriculumId = in.readInt();
		int pairsAmount = in.readInt();
		for (int pairNumber = 0; pairNumber < pairsAmount; pairNumber++) {
			EntityPair pair = new EntityPair();
			pair.setEntity1(in.readUTF());
			pair.setEntity2(in.readUTF());
			pairs.add(pair);
		}
	}

}
