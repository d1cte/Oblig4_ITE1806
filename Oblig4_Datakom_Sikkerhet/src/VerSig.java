import java.io.*;
import java.security.*;
import java.security.spec.*;

class VerSig {

    public static void main(String[] args) {

        /* Verify a DSA signature */

        if (args.length != 3) {
            System.out.println("Usage: VerSig " +
                "publickeyfile signaturefile " + "datafile");
        }
        else try {

        	// Read in encoded public key
        	FileInputStream keyfis = new FileInputStream(args[0]);
        	byte[] encKey = new byte[keyfis.available()];  
        	keyfis.read(encKey);

        	keyfis.close();
        	
        	// Key specification
        	X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        	
        	KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
        	
        	PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        	
        	// Input signature
        	FileInputStream sigfis = new FileInputStream(args[1]);
        	byte[] sigToVerify = new byte[sigfis.available()]; 
        	sigfis.read(sigToVerify);
        	sigfis.close();

        	// Initialize signature object for verification
        	Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
        	sig.initVerify(pubKey);
        	
        	// Supply the Signature Object with the Data to be verified
        	FileInputStream datafis = new FileInputStream(args[2]);
        	BufferedInputStream bufin = new BufferedInputStream(datafis);

        	byte[] buffer = new byte[1024];
        	int len;
        	while (bufin.available() != 0) {
        	    len = bufin.read(buffer);
        	    sig.update(buffer, 0, len);
        	};

        	bufin.close();
        	
        	// Verify the signature
        	boolean verifies = sig.verify(sigToVerify);

        	System.out.println("signature verifies: " + verifies);

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

}