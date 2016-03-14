package br.ufsc.egc.curriculumextractor.model;

public class EntityPairCoocurrence {

	private EntityPair entityPair;
	private int frequency;
	
	public EntityPairCoocurrence() {
		entityPair = new EntityPair();
		frequency = 0;
	}

	public EntityPair getEntityPair() {
		return entityPair;
	}

	public void setEntityPair(EntityPair entityPair) {
		this.entityPair = entityPair;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getEntity1() {
		return entityPair.getEntity1();
	}

	public void setEntity1(String entity1) {
		entityPair.setEntity1(entity1);
	}

	public String getEntity2() {
		return entityPair.getEntity2();
	}

	public void setEntity2(String entity2) {
		entityPair.setEntity2(entity2);
	}
	
	public void increment() {
		frequency++;
	}

	@Override
	public String toString() {
		return "EntityPairCoocurrence [entityPair=" + entityPair
				+ ", frequency=" + frequency + "]";
	}

}
