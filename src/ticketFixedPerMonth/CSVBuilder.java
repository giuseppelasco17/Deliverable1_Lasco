package ticketFixedPerMonth;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

import org.json.JSONException;

import com.opencsv.CSVWriter;

public class CSVBuilder {
	private String path;
	private String repository;
	
	public String getPath() {
		return path;
	}
	
	public String getRepository() {
		return repository;
	}
	
	private void loadPath(){
		try {
			FileReader f;
		    f = new FileReader("config.txt");
		    BufferedReader buff = new BufferedReader(f);
			this.path = buff.readLine();
			this.repository = buff.readLine();
			buff.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		CSVBuilder csvbuild = new CSVBuilder();
		RetrieveTicketsID rtvTkt = new RetrieveTicketsID();
		csvbuild.loadPath();
		String path = csvbuild.getPath();
		String repository = csvbuild.getRepository();
		GitQuery gitQuery = new GitQuery();
		TreeMap<String,Integer> tktToDateMap = new TreeMap<String,Integer>();
		gitQuery.gitClone(path, repository);
		gitQuery.gitPull(path, repository);
		ArrayList<String> IDList = new ArrayList<String>();
		try {
			IDList = rtvTkt.retrieveTicketsIDs();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String ID : IDList) {
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
			e.printStackTrace();
		}
		
	}

}
