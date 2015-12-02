package com.ibaguo.mqa.solr;

import java.io.FileWriter;
import java.io.IOException;

public class WriterThread extends Thread {
	String fileName;
	String content;

	public WriterThread(String fileName, String content) {
		super();
		this.fileName = fileName;
		this.content = content;
	}

	@Override
	public void run() {
		try {
			FileWriter fw = new FileWriter(fileName);
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	};
}