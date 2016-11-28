import java.io.*;
import java.security.*;

class GenSig {

    public static void main(String[] args) {

        /* Generate a DSA signature */

        if (args.length != 1) {
            System.out.println("Usage: GenSig nameOfFileToSign");
        }
        else try {
        	// Create keypair generator
        	KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        	
        	// Initialize keypair generator
        	SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        	keyGen.initialize(1024, random);
        	
        	// Generate the pair of keys
        	KeyPair pair = keyGen.generateKeyPair();
        	PrivateKey priv = pair.getPrivate();
        	PublicKey pub = pair.getPublic();

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
}