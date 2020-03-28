package project_package;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.opencsv.CSVWriter;

public class CSVBuilder {
	private String path;
	private String repository;
	
	static Logger logger = Logger.getAnonymousLogger();
	
	private static final String EXCEPTION_THROWN = "an exception was thrown";

	
	public String getPath() {
		return path;
	}
	
	public String getRepository() {
		return repository;
	}
	
	private void loadPath(){		
		try (
			FileReader f = new FileReader("config.txt");
			BufferedReader buff = new BufferedReader(f);
			){
			this.path = buff.readLine();
			this.repository = buff.readLine();
			buff.close();
			f.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, EXCEPTION_THROWN, e);
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
		List<String> iDList = new ArrayList<>();
		try {
			iDList = rtvTkt.retrieveTicketsIDs();
		} catch (JSONException | IOException e) {
			logger.log(Level.SEVERE, EXCEPTION_THROWN, e);
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
			for(Entry<String, Integer> entry : tktToDateMap.entrySet()) {
				String[] data = {entry.getKey() , String.valueOf(entry.getValue())};
				writer.writeNext(data);
			}
			writer.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, EXCEPTION_THROWN, e);
		}
		
	}
}
