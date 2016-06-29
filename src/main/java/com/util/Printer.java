package com.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Printer {
	private PrintWriter printWriter;
	/**
	 * @param fileName
	 * Creates a printer instance dedicated to a file
	 */
	public Printer(String fileName) {
		try {
			this.printWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		} catch (FileNotFoundException e) {
			System.err.println("Exception "+e.getClass()+" occurred while trying to open file "+fileName);
		} catch (IOException e) {
			System.err.println("Exception "+e.getClass()+" occurred while trying to open file "+fileName);
		}
	}
	/**
	 * @param value
	 * Writes the value specified only if the value is not empty
	 */
	public void write(String value) {
		if(!"".equalsIgnoreCase(value)) {
			printWriter.write(value+"\n");
		}
	}
	/**
	 * 
	 */
	public void close() {
		printWriter.close();
	}
	/**
	 * 
	 */
	public void flush() {
		printWriter.flush();
	}
}
