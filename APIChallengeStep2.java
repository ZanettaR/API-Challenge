
package apichallengestep2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author nettariana
 * 
 * Step 2: Code2040 API Challenge Java
 * Sends an HTTP Request to the specified endpoint
 * Then takes the string that is returned,
 * And reverses it then sends it back to be validated.
 */
public class APIChallengeStep2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Gets the original word provided by calling the getHTTPResponse helper 
        String original = getHTTPResponse();
        
        //Will hold the reversal of the original string
        String reverse = "";
        //Reads the original string from end to beginning and individually adds each letter to the reversed string
        for (int i = original.length() - 1; i >= 0; i--) {
            reverse += original.charAt(i);
        }
        //Sends the reversed word  by calling the sendHTTPResponse helper 
        sendFinalHTTPResponse(reverse);
    }

    public static void sendFinalHTTPResponse(String reverse) {

        try {

            //Creates a url object out of the designated endpoint
            URL url = new URL("http://challenge.code2040.org/api/reverse/validate");
            //Opens a connection between the program and the endpoint
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setConnectTimeout(5000);
            connect.setReadTimeout(5000);
            //Sets the type of request that is being made
            connect.setRequestMethod("POST");
            connect.setDoOutput(true);
           
            //Sets the request header
            connect.setRequestProperty("Content-Type", "application/json");
            
            //Uses an output stream in order to write the HTTP request body that contains json dictionary
            OutputStreamWriter write = new OutputStreamWriter(connect.getOutputStream());
            write.write("{"
                    + "\"token\":"
                    + "\"2e7c1620f7482d6a260d512a33bdf5e1\","
                    + "\"string\":"
                    + '"' + reverse + '"'
                    + "}");
            write.flush();
            write.close();

           //Uses an InputStream object and Buffered reader object in order to get the response from the request
            InputStream stream = connect.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
            String response;
            while ((response = buffer.readLine()) != null) {
                System.out.println(response);

            }

            connect.disconnect();

        } catch (Exception ex) {
        }
    }

    //Method will return the provided word that is to be reversed
    public static String getHTTPResponse() {
        //Holds the original word that is going to be returned
        String original = null;

        try {
            
            //Creates a url object out of the designated endpoint
            URL url = new URL("http://challenge.code2040.org/api/reverse");
            //Opens a connection between the program and the endpoint
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setConnectTimeout(5000);
            connect.setReadTimeout(5000);
            //Sets the type of request that is being made
            connect.setRequestMethod("POST");
            connect.setDoOutput(true);
           
            //Sets the request header
            connect.setRequestProperty("Content-Type", "application/json");
            
            //Uses an output stream in order to write the HTTP request body that contains json dictionary
            OutputStreamWriter write = new OutputStreamWriter(connect.getOutputStream());
            write.write("{"
                    + "\"token\":"
                    + "\"2e7c1620f7482d6a260d512a33bdf5e1\""
                    + "}");
            write.flush();
            write.close();
            
            //Uses an InputStream object and Buffered reader object in order to get the response from the request
            InputStream stream = connect.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
            String r;
            while ((r = buffer.readLine()) != null) {
                System.out.println(r);
                original = r;

            }
            
            //Closes the connection
            connect.disconnect();

        } catch (Exception ex) {
        }
        
        //returns the string that is going to be reveresed
        return original;
    }
}
