package br.ufsc.egc.curriculumextractor.model.json;

import java.util.Arrays;
import java.util.List;

public class TermComparator {
	
	List<String> tree1 = Arrays.asList(new String[]{"Administração", "Administração", "Agricultura", "Agronomia", "Agronomia", "Análise", "Biologia", "Bioquímica", "Ciência", "Ciências", "Ciências agrárias", "Ciências exatas", "Cultura", "Doenças", "Economia", "Educação", "Engenharia", "Física", "Inovação", "Medicina", "Pedagogia", "Pesquisa", "Planejamento", "Plantas", "Projetos", "Química", "Química", "Sociologia", "Solo", "Taxonomia", "Tecnologia", "Tecnologia", "Zootecnia", "Zootecnia"});
	List<String> tree2 = Arrays.asList(new String[]{"Administração", "Administração", "Agricultura", "Agronomia", "Agronomia", "Análise", "Biologia", "Bioquímica", "Ciência", "Ciências", "Ciências agrárias", "Ciências exatas", "Cultura", "Doenças", "Economia", "Educação", "Engenharia", "Física", "Inovação", "Inovação", "Medicina", "Pedagogia", "Pesquisa", "Planejamento", "Plantas", "Projetos", "Química", "Sociologia", "Solo", "Taxonomia", "Tecnologia", "Tecnologia", "Zootecnia", "Zootecnia"});
	
	public static void main(String[] args) {
		new TermComparator().run();
	}

	public void run() {
		for (int i = 0; i < tree1.size(); i++) {
			if (!tree2.contains(tree1.get(i))) {
				System.out.println(tree1.get(i));
			}
		}
		for (int i = 0; i < tree2.size(); i++) {
			if (!tree1.contains(tree2.get(i))) {
				System.out.println(tree2.get(i));
			}
		}
	}

}
