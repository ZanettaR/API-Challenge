
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author nettariana
 */
public class APIChallengeStep3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Gets the dictionary string provided by calling the getHTTPResponse helper 
        String s = getHTTPResponse();

        //Replaces the first ',' in the program with something unique so I can split the string between the needle and the haystack
        s = s.replaceFirst(",", ",,");

        //Gets rid of all excess punctuation in the returned dictionary string
        s = s.replace("\"", "");
        s = s.replace("{", "");
        s = s.replace("}", "");
        s = s.replace("[", "");
        s = s.replace("]", "");

        //Splits the string into needle key and value and haystack key and value
        String[] arr = s.split(",,");

        //Will hold the string needle
        String needle = "";

        //Will hold the haystack value in the form of a list
        List<String> haystack = new ArrayList<>();

        //Searches through the array holding two strings containing the two dictionary key values
        for (String string : arr) {

            //if the string contains 'needle' remove everything except the value... Form: needle:<some string value>
            if (string.contains("needle")) {
                needle = string.replace("needle:", "");
            }

            //if the string contains 'haystack' remove everything except the list values... Form: haystack:<string1,string2,...,stringN>, where N = some natural number
            if (string.contains("haystack")) {
                String holder = string.replace("haystack:", "");

                //Split the string of values and store them in a list object
                String[] h = holder.split(",");
                haystack = Arrays.asList(h);
            }
        }
        //get the index of a particular string in the list
        int index = haystack.indexOf(needle);
        //Sends the index of the needle in the haystack by calling the sendHTTPResponse helper 
        sendFinalHTTPResponse(index);

    }

    //Method that sends the index of the needle in the haystack
    public static void sendFinalHTTPResponse(int needle) {

        try {

            //Creates a url object out of the designated endpoint
            URL url = new URL("http://challenge.code2040.org/api/haystack/validate");
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
                    + "\"needle\":"
                    + '"' + needle + '"'
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

    //Method will return the provided dictionary of needle and haystack
    public static String getHTTPResponse() {
        String s = null;

        try {

            //Creates a url object out of the designated endpoint
            URL url = new URL("http://challenge.code2040.org/api/haystack");
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
                s = r;

            }

            //Closes the connection
            connect.disconnect();

        } catch (Exception ex) {
        }

        return s;
    }

}
