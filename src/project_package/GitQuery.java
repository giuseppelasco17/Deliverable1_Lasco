package project_package;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GitQuery {

		String s;
		
		static Logger logger = Logger.getAnonymousLogger();

		public String logFilter(String path, String ticket) {
			String s = null;
			try {
				//System.out.println("I'm printing all the IDs of the commits (if any) having the word 'Added'");
				//First line represent the last commit date
				String pathComplete = path + "\\libcloud";
				Process p = Runtime.getRuntime().exec("git -C " + pathComplete + " log -1 --pretty=format:\"%cs\" --grep=" + ticket);
				p.waitFor();
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				s = stdInput.readLine();
			} catch (IOException | InterruptedException e) {
				logger.log(Level.SEVERE, "an exception was thrown", e);
	            // Restore interrupted state...
	            Thread.currentThread().interrupt();
	            System.exit(-1);
			}
			return s;
		}
		
		public void gitClone(String path, String repository) {
			Process p;
			try {
				new File(path).mkdir();
				p = Runtime.getRuntime().exec("git -C " + path + " clone " + repository);
				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}
				p.waitFor();
				
			} catch (IOException e) {
				logger.log(Level.SEVERE, "an exception was thrown", e);
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.log(Level.SEVERE, "an exception was thrown", e);
			}
		
		}
		
		public void gitPull(String path, String repository) {
			Process p;
			try {
				p = Runtime.getRuntime().exec("git -C " + path + " pull " + repository);
				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}
				p.waitFor();
				
			} catch (IOException e) {
				logger.log(Level.SEVERE, "an exception was thrown", e);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "an exception was thrown", e);
	            Thread.currentThread().interrupt();
			}

		}

}
