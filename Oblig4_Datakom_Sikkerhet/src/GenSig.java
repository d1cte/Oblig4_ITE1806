import java.io.*;
import java.security.*;
/* 	
 * Har brukt eksempelkoden fra oracle.com
*/
class GenSig {


    public static void main(String[] args) {
    	String keystoreName = "mykeystore";
    	char[] keystorePassword = {'p', 'a', 's', 's', 'o', 'r', 'd'};
    	
        if (args.length != 1) {
            System.out.println("Usage: GenSig nameOfFileToSign");
        }
        else try {
        	// keystore
        	KeyStore keystore = KeyStore.getInstance("JKS");
        	FileInputStream keystoreFis = new FileInputStream(keystoreName);
        	BufferedInputStream keystoreBufin = new BufferedInputStream(keystoreFis);
        	keystore.load(keystoreBufin, keystorePassword);
        	
        	PrivateKey privateKey = (PrivateKey) keystore.getKey("bk", keystorePassword);
        	
        	java.security.cert.Certificate certificate = keystore.getCertificate("bk");
        	
        	PublicKey publicKey = certificate.getPublicKey();
        	
        	
        	// Signature
        	Signature dsa = Signature.getInstance("SHA256withRSA");
        	
        	dsa.initSign(privateKey);
        	
        	// Supply the Signature Object the Data to be Signed
        	FileInputStream fis = new FileInputStream(args[0]);
        	BufferedInputStream bufin = new BufferedInputStream(fis);
        	byte[] buffer = new byte[1024];
        	int len;
        	while ((len = bufin.read(buffer)) >= 0) {
        		dsa.update(buffer, 0, len);
        	};
        	bufin.close();
        	
        	// Generate the Signature
        	byte[] realSig = dsa.sign();
        	
        	/* save the signature in a file */
        	FileOutputStream sigfos = new FileOutputStream("signature");
        	sigfos.write(realSig);
        	sigfos.close();
     	
//        	/* save the public key in a file */
//        	byte[] key = publicKey.getEncoded();
//        	FileOutputStream keyfos = new FileOutputStream("bkPublicKey");
//        	keyfos.write(key);
//        	keyfos.close();
 
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
    

        


    
}