package project_package;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.opencsv.CSVWriter;

public class CSVBuilder {
	private String path;
	private String repository;
	
	static Logger logger = Logger.getAnonymousLogger();

	
	public String getPath() {
		return path;
	}
	
	public String getRepository() {
		return repository;
	}
	
	private void loadPath(){
		FileReader f = null;
		BufferedReader buff = null;
		try {
		    f = new FileReader("config.txt");
		    buff = new BufferedReader(f);
			this.path = buff.readLine();
			this.repository = buff.readLine();
			buff.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				f.close();
				buff.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "an exception was thrown", e);
			}
		}
	}
	public static void main(String[] args) {
		CSVBuilder csvbuild = new CSVBuilder();
		RetrieveTicketsID rtvTkt = new RetrieveTicketsID();
		csvbuild.loadPath();
		String path = csvbuild.getPath();
		String repository = csvbuild.getRepository();
		GitQuery gitQuery = new GitQuery();
		TreeMap<String,Integer> tktToDateMap = new TreeMap<>();
		gitQuery.gitClone(path, repository);
		gitQuery.gitPull(path, repository);
		ArrayList<String> iDList = new ArrayList<>();
		try {
			iDList = rtvTkt.retrieveTicketsIDs();
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "an exception was thrown", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "an exception was thrown", e);
		}
		for(String ID : iDList) {
			String date = gitQuery.logFilter(path, ID);
			String formattedDate = null;
			if(date != null) {
				formattedDate = date.substring(0,7);
				if(tktToDateMap.get(formattedDate) != null) {
					int val = tktToDateMap.get(formattedDate);
					tktToDateMap.put(formattedDate, val + 1);
				}else {
					tktToDateMap.put(formattedDate, 1);
				}
			}
		}
		File file = new File("result.csv");
		FileWriter outputfile;
		try {
			outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile);
			String[] header = {"Dates", "#Fixed tickets"};
			writer.writeNext(header);
			for(String k : tktToDateMap.keySet()) {
				String[] data = {k , String.valueOf(tktToDateMap.get(k))};
				writer.writeNext(data);
			}
			writer.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "an exception was thrown", e);
		}
		
	}
}
