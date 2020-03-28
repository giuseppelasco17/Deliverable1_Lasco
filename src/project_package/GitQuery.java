package project_package;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GitQuery {
	
	private static final String CMD = "git -C ";
	
	private static final String EXCEPTION_THROWN = "an exception was thrown";
	
	static Logger logger = Logger.getAnonymousLogger();

	public String logFilter(String path, String ticket) {
		String tkt = null;
		try {
			//First line represent the last commit date
			String pathComplete = path + "\\libcloud";
			Process p = Runtime.getRuntime().exec(CMD + pathComplete + " log -1 --pretty=format:\"%cs\" --grep=" + ticket);
			p.waitFor();
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			tkt = stdInput.readLine();
		} catch (IOException | InterruptedException e) {
			logger.log(Level.SEVERE, EXCEPTION_THROWN, e);
            // Restore interrupted state...
            Thread.currentThread().interrupt();
            System.exit(-1);
		}
		return tkt;
	}
	
	public void gitClone(String path, String repository) {
		Process p;
		try {
			new File(path).mkdir();
			p = Runtime.getRuntime().exec(CMD + path + " clone " + repository);
			p.waitFor();
		} catch (IOException e) {
			logger.log(Level.SEVERE, EXCEPTION_THROWN, e);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, EXCEPTION_THROWN, e);
			Thread.currentThread().interrupt();
		}
	
	}
	
	public void gitPull(String path, String repository) {
		Process p;
		try {
			p = Runtime.getRuntime().exec(CMD + path + " pull " + repository);
			p.waitFor();
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, EXCEPTION_THROWN, e);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, EXCEPTION_THROWN, e);
            Thread.currentThread().interrupt();
		}

	}

}
