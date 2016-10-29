package apichallengestep5;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 *
 * @author nettariana
 */
public class APIChallengeStep5 {

    public static void main(String[] args) {
        String s = getHTTPResponse();

        //Splits the string into datestamp key and value and interval key and value
        s = s.replace("{", "");
        s = s.replace("}", "");
        s = s.replaceAll("\"", "");
        //Will hold the date portion of the datestamp key value
        String date = "";
        //Will hold the time portion of the datestamp key value
        String time = "";
        //Will hold the value of the interval key
        int seconds = 0;
        //Splits the string into an array of size two holding the two dictionary key values
        String[] arr = s.split(",");

        for (String string : arr) {
            //Gets the value of the string and separates it into a time and date
            if (string.contains("datestamp")) {
                string = string.replaceAll("datestamp:", "");
                date = string.substring(0, string.indexOf("T"));
                time = string.substring(string.indexOf("T") + 1, string.length()-1);
            }
            if (string.contains("interval")) {

                seconds = Integer.parseInt(string.replaceAll("interval:", ""));
            }
        }
        //Puts the time and date into separate string arrays
        String[] dates = date.split("-");
        String[] t = time.split(":");
    

        //Creates a string date by calling the createDateString method
        String d = createDateString(dates, t, seconds);
        System.out.println(d);
        sendFinalHTTPResponse(d);
    }

    /**
     * This method takes in a string array of a year, month, and day that make up a date,
     * a string array of the time made up of hours, minutes, and days, and an integer of seconds
     * The approach I chose to take was to to first convert the entire time to seconds and add that to the
     * new seconds interval
     * 
     * Once I'd done that I simply calculated the overlap between the normal amount
     * of months, days, hours, minutes, and seconds in a day
     * Finally in order to math ISO format I simply
     * concatenated on a "0" in front of any single digit values.
     */
    
    public static String createDateString(String[] date, String[] time, int seconds) {
        int days = Integer.parseInt(date[2]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[0]);
        if(Integer.parseInt(time[0]) == 0){
            time[0]= "24";
        }
        seconds += (Integer.parseInt(time[0]) * 3600) + (Integer.parseInt(time[1]) * 60) + Integer.parseInt(time[2]);
        int hours = seconds / 3600;
        seconds = seconds - (3600 * hours);
        int minutes = seconds / 60;
        seconds = seconds - (60 * minutes);
        if(hours == 24){
            hours = 0;
            days += 1;
        }
        
        if (hours > 24) {

            days += hours / 24;
            hours = hours - 24 * (hours / 24);
        }

        if (days > 31 && (month == 1||month == 3||month == 5||month == 7||month == 8||month == 10||month == 12)) {
            month += days / 31;
            days = days - 31 * (days / 31);

        }
        if (days > 30 && (month == 4 || month == 6 || month == 9 || month == 11)) {
            month += days / 30;
            days = days - 30 * (days / 30);

        }
        //Takes care of leap years
        if(year % 400 == 0 && month == 2 && days > 29){
            month += days / 29;
            days = days - 28 * (days / 29);
        }
        if(year % 400 != 0 && month == 2 && days > 28){
            month += days / 28;
            days = days - 28 * (days / 28);
        }
        if (month > 12) {
            year += month / 12;
            month = month - 12 * (month / 12);
        }

        String m = "" + month;
        String d = "" + days;
        String h = "" + hours;
        String mn = "" + minutes;
        String s = "" + seconds;

        if (month < 10) {
            m = "0" + m;
        }
        if (days < 10) {
            d = "0" + d;
        }
        if (hours < 10) {
            h = "0" + h;
        }

        if (minutes < 10) {
            mn = "0" + mn;
        }
        if (seconds < 10) {
            s = "0" + s;
        }

        return year + "-" + m + "-" + d + "T" + h + ":" + mn + ":" + s + "Z";
    }

    public static String getHTTPResponse() {
        String s = null;

        try {

            //Creates a url object out of the designated endpoint
            URL url = new URL("http://challenge.code2040.org/api/dating");
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

    public static void sendFinalHTTPResponse(String date) {

        try {

            //Creates a url object out of the designated endpoint
            URL url = new URL("http://challenge.code2040.org/api/dating/validate");
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
                    + "\"datestamp\":"
                    + "\"" + date + "\""
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

}
