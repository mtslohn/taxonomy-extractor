package br.ufsc.egc.curriculumextractor.model;

import java.util.Objects;

public class EntityPair {

	private String entity1;
	private String entity2;

	public EntityPair() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EntityPair(String entity1, String entity2) {
		super();
		this.entity1 = entity1;
		this.entity2 = entity2;
	}

	public String getEntity1() {
		return entity1;
	}

	public void setEntity1(String entity1) {
		this.entity1 = entity1;
	}

	public String getEntity2() {
		return entity2;
	}

	public void setEntity2(String entity2) {
		this.entity2 = entity2;
	}

	@Override
	public String toString() {
		return "EntityPair [entity1=" + entity1 + ", entity2=" + entity2 + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entity1 == null) ? 0 : entity1.hashCode());
		result = prime * result + ((entity2 == null) ? 0 : entity2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityPair other = (EntityPair) obj;
		if (entity1 == null) {
			if (other.entity1 != null)
				return false;
		} else if (!entity1.equals(other.entity1))
			return false;
		if (entity2 == null) {
			if (other.entity2 != null)
				return false;
		} else if (!entity2.equals(other.entity2))
			return false;
		return true;
	}
	
}