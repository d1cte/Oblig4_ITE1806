import java.io.*;
import java.security.*;

class VerSig {
	static String keystoreName = "friendskeystore";
	static KeyStore keystore;
	static char[] keystorePassword;
	
	static String certificateAlias;
	static java.security.cert.Certificate certificate;
	
	static PublicKey publicKey;

    public static void main(String[] args) {
    	
        if (args.length != 4) {
            System.out.println("Usage: VerSig " + "certificateAlias " + "password " + "signaturefile " + "datafile");
        }
        else try {
        	certificateAlias = args[0];
        	keystorePassword = args[1].toCharArray();
        	String signatureFile = args[2];
        	String dataFile = args[3];
        	
        	inputKeystore();
        	setCertificateAndPublicKey();
        
        	byte[] signatureToVerify = inputSignatureBytes(signatureFile);
        	
        	// Initialize signature object for verification
        	Signature signature = Signature.getInstance("SHA256WITHRSA", "SunRsaSign");
        	signature.initVerify(publicKey);
        	
        	boolean verifies = verifySignatureWithData(signature, signatureToVerify, dataFile);

        	System.out.println("signature verifies: " + verifies);


        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
    public static void inputKeystore() throws Exception {
    	keystore = KeyStore.getInstance("JKS");
    	FileInputStream keystoreFis = new FileInputStream(keystoreName);
    	BufferedInputStream keystoreBufin = new BufferedInputStream(keystoreFis);
    	keystore.load(keystoreBufin, keystorePassword);
    }
    
    public static void setCertificateAndPublicKey() throws Exception {
    	certificate = keystore.getCertificate(certificateAlias);
    	publicKey = certificate.getPublicKey();
    }
    
    public static byte[] inputSignatureBytes(String signatureFile) throws Exception {
    	// Input signature bytes
    	FileInputStream signatureFis = new FileInputStream(signatureFile);
    	byte[] signatureToVerify = new byte[signatureFis.available()]; 
    	signatureFis.read(signatureToVerify);
    	signatureFis.close();
    	return signatureToVerify;
    }
    
    public static boolean verifySignatureWithData(Signature signature, byte[] signatureToVerify,  String dataFile) throws Exception {
    	// Supply the Signature Object with the Data to be verified
    	FileInputStream datafis = new FileInputStream(dataFile);
    	BufferedInputStream bufin = new BufferedInputStream(datafis);

    	byte[] buffer = new byte[1024];
    	int len;
    	while (bufin.available() != 0) {
    	    len = bufin.read(buffer);
    	    signature.update(buffer, 0, len);
    	};

    	bufin.close();
    	
    	// Verify the signature    	
    	return signature.verify(signatureToVerify);
    }
}