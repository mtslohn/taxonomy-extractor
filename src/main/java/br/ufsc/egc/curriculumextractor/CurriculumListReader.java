package br.ufsc.egc.curriculumextractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mapdb.DBMaker;

public class CurriculumListReader {

	public static final String CURRICULUM_LIST_TXT = "resources/saida.txt";

	public Map<Integer, String> read() {
		return read(-1); 
	}
	
	public Map<Integer, String> read(int readLinesLimit) {

		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		Map<Integer, String> curriculumMap = DBMaker.newTempTreeMap();

		try {
			
			fileReader = new FileReader(new File(CURRICULUM_LIST_TXT));
			bufferedReader = new BufferedReader(fileReader);
			
			
			String line = bufferedReader.readLine();
			int key = 0;
			
			while (line != null && (readLinesLimit < 0 || key < readLinesLimit)) {
				
				if (!line.startsWith("=")) {
					curriculumMap.put(key, line);
					key++;
				}
				
				line = bufferedReader.readLine();
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return curriculumMap;

	}

}
