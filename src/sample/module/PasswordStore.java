package sample.module;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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
			passwords.put(user[0], user[1] + ":" + user[2]);
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
		String pwd = user[0];
		String salt = user[1];
		// test pwd
		return testPassword(password, pwd, salt);
	}
	
	private boolean testPassword(String entered, String stored, String salt) {
		try {
			String encrypted = encrypt(entered, salt);
			return stored.equals(encrypted);
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private String generateSalt() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[8];
		sr.nextBytes(salt);
		return new String(salt, "UTF-8");
	}
	
	private String encrypt(String password, String salt) throws NoSuchAlgorithmException {
		String msg = password + salt;
		return md5(msg);
	}
	
	private String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(input.getBytes(), 0, input.length());
        String md5 = new BigInteger(1, digest.digest()).toString(16);
        return md5;
    }
	
	public static void main(String[] args) throws Exception {
		PasswordStore pstore = PasswordStore.getInstance();
		String salt = pstore.generateSalt();
		String digest = pstore.encrypt("asdf", salt);
		System.out.println(digest + ":" + salt);
		System.out.println("Testing user 'test'...");
		System.out.println("password: 'qwer'");
		System.out.println("Result:");
		System.out.println(pstore.validatePassword("test", "asdf"));
	}
}
