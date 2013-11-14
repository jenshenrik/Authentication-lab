package sample.module;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class PasswordStore {
	
	private static PasswordStore instance;
	private final static String pwdFilePath = "passwd";
	
	private HashMap<String, String> passwords;
	
	private PasswordStore(String path) {
		this.passwords = new HashMap<String, String>();
		
		try {
			readFile(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static PasswordStore getInstance() {
		if (instance == null) {
			instance = new PasswordStore(pwdFilePath);
		}
		return instance;
	}
	
	private void readFile(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path), 
				Charset.defaultCharset());
		for (int i = 0; i < lines.size(); i++) {
			String[] user = lines.get(i).split(":");
			passwords.put(user[0], user[1]);
		}
	}
	
	public boolean validateUsername(String username) {
		return passwords.containsKey(username);
	}
	
	public boolean validatePassword(String username, String password) {
		String pwdLine = passwords.get(username);
		// return false if username not found
		if (pwdLine == null)
			return false;
		
		// split pwd string
		String[] user = pwdLine.split(":");
		String pwd = user[1];
		String hash = user[2];
		
		// test pwd
		
		// return result
		
		return true;
	}
	
	/*
	public static void main(String[] args) {
		PasswordStore pstore = PasswordStore.getInstance();
	}
	*/
}
