import java.io.*;
import java.security.*;

class VerSig {

    public static void main(String[] args) {
    	String keystoreName = "friendskeystore";
    	char[] keystorePassword = {'p', 'a', 's', 's', 'o', 'r', 'd'};

        if (args.length != 2) {
            System.out.println("Usage: VerSig " + "signaturefile " + "datafile");
        }
        else try {
        	// keystore
        	KeyStore keystore = KeyStore.getInstance("JKS");
        	FileInputStream keystoreFis = new FileInputStream(keystoreName);
        	BufferedInputStream keystoreBufin = new BufferedInputStream(keystoreFis);
        	keystore.load(keystoreBufin, keystorePassword);
        	
        	java.security.cert.Certificate certificate = keystore.getCertificate("bkcertificate");
        	
        	PublicKey publicKey = certificate.getPublicKey();
//        	// Read in encoded public key
//        	FileInputStream keyfis = new FileInputStream(args[0]);
//        	byte[] encKey = new byte[keyfis.available()];  
//        	keyfis.read(encKey);
//
//        	keyfis.close();
        	
//        	// Key specification
//        	X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKey);
//        	
//        	KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
//        	
//        	PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        	
        	// Input signature bytes
        	FileInputStream signatureFis = new FileInputStream(args[0]);
        	byte[] signatureToVerify = new byte[signatureFis.available()]; 
        	signatureFis.read(signatureToVerify);
        	signatureFis.close();

        	// Initialize signature object for verification
        	Signature signature = Signature.getInstance("SHA256withRSA", "SUN");
        	signature.initVerify(publicKey);
        	
        	// Supply the Signature Object with the Data to be verified
        	FileInputStream datafis = new FileInputStream(args[1]);
        	BufferedInputStream bufin = new BufferedInputStream(datafis);

        	byte[] buffer = new byte[1024];
        	int len;
        	while (bufin.available() != 0) {
        	    len = bufin.read(buffer);
        	    signature.update(buffer, 0, len);
        	};

        	bufin.close();
        	
        	// Verify the signature
        	boolean verifies = signature.verify(signatureToVerify);

        	System.out.println("signature verifies: " + verifies);

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

}