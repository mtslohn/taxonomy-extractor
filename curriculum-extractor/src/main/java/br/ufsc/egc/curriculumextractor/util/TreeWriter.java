package br.ufsc.egc.curriculumextractor.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;

public class TreeWriter {

	private static final String SPACER = "\n\n========\n\n";
	private static final String FILE_NAME_TEMPLATE = "results/txt/%s-%s.txt";
	public static final String DATE_FORMAT = "yyyy-MM-dd.HH.mm.ss";

	public void write(String strategyName, NERMetrics nerMetrics, Set<String> cyclicWords, Tree tree) {

		File file = new File(createFileName(strategyName));

		file.getParentFile().mkdirs();

		FileWriter fileWriter = null;
		BufferedWriter buffWriter = null;
		try {
			
			fileWriter = new FileWriter(file);
			buffWriter = new BufferedWriter(fileWriter);
			
			TreeMetrics metrics = new TreeMetrics(tree, cyclicWords);

			buffWriter.write(nerMetrics.print());
			buffWriter.write(SPACER);
			buffWriter.write(metrics.print());
			buffWriter.write(SPACER);
			buffWriter.write(tree.print());
			
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

	private String createFileName(String strategyName) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return String.format(FILE_NAME_TEMPLATE, strategyName,
				sdf.format(Calendar.getInstance().getTime()));
	}
}
