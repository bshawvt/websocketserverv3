package server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class SSL { // https://github.com/TooTallNate/Java-WebSocket/wiki/Getting-a-SSLContext-from-different-sources#getting-a-sslcontext-using-a-lets-encrypt-certificate
	/* steps for future reference because this was a major pain in my fat ass
	 * install python, run commandline: 'pip install certbot'
	 * followed by 'certbot certonly'
	 * complete short questionaire, select the "place files in webroot directory" option when asked because it worked and we're not using normal webserver
	 * now set paths in websocketserverv3 and done! */
	/* with my personal router, port 443(this servers SSL port) must be open or TLS handshake will fail */
	/**
	* Method which returns a SSLContext from a Let's encrypt or IllegalArgumentException on error
	*
	* @return a valid SSLContext
	* @throws IllegalArgumentException when some exception occurred 
	*/
	public SSLContext getSSLContextFromLetsEncrypt() {
	    SSLContext context;
	    String pathTo = "E:\\acme"; // path to folder which stores certificate items
	    String keyPassword = ""; // certbot does not seem to use passwords
	    try {
	        context = SSLContext.getInstance("TLS");

	        byte[] certBytes = parseDERFromPEM(Files.readAllBytes(new File(pathTo + File.separator + "cert.pem").toPath()), "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
	        byte[] keyBytes = parseDERFromPEM(Files.readAllBytes(new File(pathTo + File.separator + "privkey.pem").toPath()), "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");
	        //System.out.println(certBytes + "\n" + (new String(certBytes)));
	        X509Certificate cert = generateCertificateFromDER(certBytes);
	        RSAPrivateKey key = generatePrivateKeyFromDER(keyBytes);

	        KeyStore keystore = KeyStore.getInstance("JKS");
	        keystore.load(null);
	        keystore.setCertificateEntry("cert-alias", cert);
	        keystore.setKeyEntry("key-alias", key, keyPassword.toCharArray(), new Certificate[]{cert});

	        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
	        kmf.init(keystore, keyPassword.toCharArray());

	        KeyManager[] km = kmf.getKeyManagers();

	        context.init(km, null, null);
	    } catch (IOException | KeyManagementException | KeyStoreException | InvalidKeySpecException | UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException e) {
	        throw new IllegalArgumentException();
	    }        
	    return context;
	}

	protected byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter) {
	    String data = new String(pem);
	    String[] tokens = data.split(beginDelimiter);
	    tokens = tokens[1].split(endDelimiter);
	    
	    tokens[0] = tokens[0].trim();
	    
	    byte[] in = Base64.getEncoder().encode(tokens[0].getBytes());//DatatypeConverter.parseBase64Binary(tokens[0]);
	    byte[] base = Base64.getDecoder().decode(in);
	    //System.out.println("tokenAsRaw: " + tokens[0]);
	   // System.out.println("tokenAsBytes: " + tokens[0].getBytes());
	    //System.out.println("tokenAsString: " + (new String(tokens[0])));
	   // System.out.println("tokenAsBase64AsString: " + (new String(base)));
	    //System.out.println("tokenAsBase64AsBytes: " + base);
	    //System.out.println(new String(base));
	   // System.out.println(new String(Base64.getMimeDecoder().decode(tokens[0])));
	    //System.out.println("tokenAsBase64AsBytes: " + base.getBytes());
	   	//System.out.println("bp");
	   	return Base64.getMimeDecoder().decode(tokens[0]);
	   	
	   /* try {
	    	
	    	//byte[] z = DatatypeConverter.parseBase64Binary("hello world");
	    	//System.out.println("datatypeconverter: " + z + "\nraw: " + tokens[0].getBytes());
	    	//byte[] = 
	    	for(byte b : tokens[0].getBytes()) {
	    		
	    	}
	    	//byte[] t = Base64.getDecoder().decode(tokens[0].getBytes());
		    //System.out.println("base64: " + t + "\nraw: " + (new String(t)));
		    
		   // String nt = new String(t);
		   // System.out.println(nt);
		    //System.out.println("\nbase64 decode for aa" + new String(Base64.getDecoder().decode("aa").toString()));
		    System.out.println("bp");
	    
	    }
	    catch(IllegalArgumentException e) {
	    	System.err.println(e + " - " + tokens[0]);
	    	
	    }*/
	    
	    //return new byte[1];
	    //return DatatypeConverter.parseBase64Binary(tokens[0]);
	   //return tokens[0].getBytes();
	}

	protected RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
	    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
	    KeyFactory factory = KeyFactory.getInstance("RSA");
	    return (RSAPrivateKey) factory.generatePrivate(spec);
	}

	protected X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException {
	    CertificateFactory factory = CertificateFactory.getInstance("X.509");

	    return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
	}
}
