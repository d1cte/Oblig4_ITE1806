import java.net.*;

import javax.net.ssl.*;


import java.io.*;
/* 	
 * Har brukt eksempelkoden fra oracle.com til både 
 * KnockKnockClient, KnockKnockServer og KnockKnockProtocol 
 * http://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
*/
public class KnockKnockServer {
	static String keystore = "keystore";
	static String keystorePassword = "passord";
	
    public static void main(String[] args) throws IOException {
         
    	
        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }
 
        int portNumber = Integer.parseInt(args[0]);
 
        try {
        	System.setProperty("javac.net.ssl.keyStore", keystore);
        	System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
        	SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(portNumber);
            
            SSLSocket clientSocket = (SSLSocket) sslServerSocket.accept();
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
         
         
            String inputLine, outputLine;
             
            // Initiate conversation with client
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);
 
            while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye."))
                    break;
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}