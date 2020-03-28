package ticketFixedPerMonth;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class GitQuery {
	//TODO per ricavare date relative ad un certo tiket git -C C:\Users\Giuseppe\Desktop\libcloud log -1 --pretty=format:"%cs" --grep="LIBCLOUD-874"

		String s;

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
				System.out.println("exception happened - here's what I know: ");
	            e.printStackTrace();
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
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
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
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

}
