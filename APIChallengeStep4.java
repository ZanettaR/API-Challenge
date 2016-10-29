import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author nettariana
 */
public class APIChallengeStep4 {

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

        //Splits the string into prefix key and value and array key and value
        String[] arr = s.split(",,");

        //Will hold the string prefix
        String prefix = "";

        //Will hold the array value in the form of a list
        List<String> array = new LinkedList<>();

        //Searches through the array holding two strings containing the two dictionary key values
        for (String string : arr) {

            //if the string contains 'prefix' remove everything except the value... Form: prefix:<some string value>
            if (string.contains("prefix")) {
                prefix = string.replace("prefix:", "");
            }

            //if the string contains 'array' remove everything except the list values... Form: array:<string1,string2,...,stringN>, where N = some natural number
            if (string.contains("array")) {
                String holder = string.replace("array:", "");

                //Split the string of values and store them in a list object
                String[] h = holder.split(",");
                array.addAll(Arrays.asList(h));
            }
        }
        
        //Iterate through the list and remove all words with the prefix specified
        ListIterator it = array.listIterator();
        int len = prefix.length();
        while (it.hasNext()) {
            String h = it.next().toString();
            if (h.substring(0, len).equalsIgnoreCase(prefix)) {
                it.remove();
            }else{
                
                //Add quotations to all other words
                h = "\""+h+"\"";
                it.set(h);
            }
        }
        //Store the new list into an array since that is what the post request is expecting
        String[]answer = new String[array.size()];
        answer = array.toArray(answer);
        sendFinalHTTPResponse(answer);
    }
    //Method that sends an array of all words not starting with the prefix
    public static void sendFinalHTTPResponse(String[] list) {

        try {

            //Creates a url object out of the designated endpoint
            URL url = new URL("http://challenge.code2040.org/api/prefix/validate");
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
                    + "\"array\":"
                    + Arrays.toString(list)
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

    public static String getHTTPResponse() {
        String s = null;

        try {

            //Creates a url object out of the designated endpoint
            URL url = new URL("http://challenge.code2040.org/api/prefix");
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
