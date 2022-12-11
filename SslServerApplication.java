package com.snhu.sslserver;
/*

 * Lawrence Arundel

 * SNHU
 * CS 305
 * 
 * Sources: 
 * 
 * Benli, B. (2020, November 6). How to enable HTTP and HTTPS in spring boot application. Java Development Journal., 
 * from https://www.javadevjournal.com/spring-boot/how-to-enable-http-https-in-spring-boot/ 
 * 
 * Omukuba, G. (2022, February 25). Implementing AES encryption and decryption in Java. Section.,
 * from https://www.section.io/engineering-education/implementing-aes-encryption-and-decryption-in-java/ 
 * 
 * Oracle, O. (2017, September 1). Java Security Standard Algorithm Names. Java security standard algorithm 
 * names., 
 * from https://docs.oracle.com/javase/9/docs/specs/security/standard-names.html#cipher-algorithm-names 
 * 
 * Oracle, O. (2021, August 1). Oracle. Oracle help center.,from https://docs.oracle.com/en/ 
 * 
 * TutorialPoint, T. P. (2022, July 1). Java cryptography - message digest. Tutorials Point.,
 * from https://www.tutorialspoint.com/java_cryptography/java_cryptography_message_digest.htm 
 * 
 */
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SslServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SslServerApplication.class, args);
	}
}

//REST controler that takes the algorithm for the AES-128 bit implementation, encrypts the proper string,
//and out puts the results in accordance to the client requirements.
@RestController
class SslServerController {
	@RequestMapping(value = "/hash") // Annotation for mapping web requests onto methods in request-handling classes
	// with flexible method signatures.
	public String myHash() throws Exception {
		// Declare variable to encrypt using AES-128 cipher/algorithm which is supported
		// by SHA-1.
		String data = "Hello World Check Sum!";
		AES_ENCRYPTION aesEncryption = new AES_ENCRYPTION();
		aesEncryption.init(); // initialize key for aes encryption.
		StringBuffer encryptedData = aesEncryption.encrypt(data);
		return "<p>data:" + data + " Name Of Cipher Algorithm Used: AES-128-Bit" + "CheckSum Value: " + encryptedData
				+ " Name: Lawrence Arundel"; // return
		// value
		// for
		// the
		// checksum
		// value.
	}

//public class that implements AES encryption for the specified design.
//requirement for 128-bit
	class AES_ENCRYPTION {
//declare variables for required key, key size, and cipher object.
		private SecretKey key;
		private final int KEY_SIZE = 128;
		private Cipher encryptionCipher;

//Designates a method of a session bean that corresponds to a create<METHOD>
//method of an adapted home or local home interface (an interface that adapts
//an EJB 2.1 or earlier EJBHome or EJBLocalHome client view respectively).
		public void init() throws Exception {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES"); // generates the instance of the AES key.
			keyGenerator.init(KEY_SIZE); // initalizes the key size used.
			key = keyGenerator.generateKey(); // generates the key given the specified parameters (AES, key_size).
		}

//encryption method for the key generated.
		public StringBuffer encrypt(String data) throws Exception {
			MessageDigest message = MessageDigest.getInstance("SHA-1"); // valid input for AES-128.
			message.update(data.getBytes()); // Pass data to the created MessageDigest object.
			byte[] digest = message.digest();// Generate the message digest.
			// In order to create a Cipher object, the application calls the Cipher's
			// getInstance
			// method, and passes the name of the requested transformation to it.
			// Optionally, the name of a provider may be specified.
			encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
			// Constant used to initialize cipher to encryption mode.
			encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
			// Finishes a multiple-part encryption or decryption operation, depending on how
			// this cipher was initialized.
			byte[] encryptedBytes = encryptionCipher.doFinal(digest);
			return byteToHex(encryptedBytes); // encode the encrypted bytes generated from the cipher by
												// converting the bytes produced to hexidecimal.
		}

//converts the data using getEncoder to properly set it into a string.
		private StringBuffer byteToHex(byte[] data) {
			StringBuffer hexString = new StringBuffer(); // Converting the byte array
															// in to HexString format.
			for (int i = 0; i < data.length; i++) {
				hexString.append(Integer.toHexString(0xFF & data[i])); // generates the hex
																		// string from specified data.
			}
			return hexString;
		}
	}
}
