import java.io.*;
import java.security.*;
/* 	
 * Har brukt eksempelkoden fra oracle.com
*/
class GenSig {
	static String keystoreName = "mykeystore";
	static KeyStore keystore;
	static char[] keystorePassword = {'p', 'a', 's', 's', 'o', 'r', 'd'};
	
	static String certificateAlias = "bk";
	static java.security.cert.Certificate certificate;
	
	static PublicKey publicKey;
	static PrivateKey privateKey;

    public static void main(String[] args) {
//    	String keystoreName = "mykeystore";
//    	char[] keystorePassword = {'p', 'a', 's', 's', 'o', 'r', 'd'};
    	
    	
        if (args.length != 1) {
            System.out.println("Usage: GenSig nameOfFileToSign");
        }
        else try {
        	importKeystore();
        	setCertificateAndKeys();

        	// Create Signature Object
        	Signature rsa = Signature.getInstance("SHA256withRSA");
        	rsa.initSign(privateKey);
        	
        	byte[] realSignature = signDataAndGenerateSignature(args[0], rsa);

        	saveSignatureToFile(realSignature, "BKs-Signature");
     	
 
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
    
    public static void importKeystore() throws Exception {
    	keystore = KeyStore.getInstance("JKS");
    	FileInputStream keystoreFis = new FileInputStream(keystoreName);
    	BufferedInputStream keystoreBufin = new BufferedInputStream(keystoreFis);
    	keystore.load(keystoreBufin, keystorePassword);
    }
    
    public static void setCertificateAndKeys() throws Exception {
    	privateKey = (PrivateKey) keystore.getKey(certificateAlias, keystorePassword);
    	certificate = keystore.getCertificate(certificateAlias);
    	publicKey = certificate.getPublicKey();
    }
    
    public static byte[] signDataAndGenerateSignature(String dataToBeSigned, Signature sigAlgorithm) throws Exception {
    	// Supply the Signature Object the Data to be Signed
    	FileInputStream fis = new FileInputStream(dataToBeSigned);
    	BufferedInputStream bufin = new BufferedInputStream(fis);
    	byte[] buffer = new byte[1024];
    	int len;
    	while ((len = bufin.read(buffer)) >= 0) {
    		sigAlgorithm.update(buffer, 0, len);
    	};
    	bufin.close();
    	
    	// Generate the Signature
    	return sigAlgorithm.sign();
    }
    
    public static void saveSignatureToFile(byte[] signature, String filename) throws Exception {
		FileOutputStream sigfos = new FileOutputStream(filename);
		sigfos.write(signature);
		sigfos.close();
    }
    
    
        
}