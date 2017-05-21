/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fritzscanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author philipp
 */
class FirstConnector {

    public static String challenge;
    public static boolean bool = false;

    public String getChallenge() {

        return challenge;
    }

    public boolean getboolean() {

        return bool;
    }

    public void run() {





        try {
            URL pageUrl = new URL("http://" + Fritzscanner.fritz + "/login_sid.lua");
            URLConnection getConn = pageUrl.openConnection();
            getConn.connect();
            BufferedReader dis = new BufferedReader(
                    new InputStreamReader(
                    getConn.getInputStream()));
            String myString;

            while ((myString = dis.readLine()) != null) {
                System.out.println(myString);
                if (myString.contains("Challenge")) {
                    Pattern MY_PATTERN = Pattern.compile(".*<Challenge>(.*)</Challenge>.*");
                    Matcher m = MY_PATTERN.matcher(myString);
                    while (m.find()) {
                        challenge = m.group(1).trim();
                        System.out.println(challenge);
                    }
                }

            }
            String Passwd = Fritzscanner.pw;
            String newStr = "";
            char[] test = Passwd.toCharArray();
            for (int i = 0, len = test.length; i < len; i++) {
                if ((int) test[i] > 255) {
                    newStr += ".";
                } else {
                    newStr += (int) test[i];
                }
            }
            //Passwd= newStr;
            String CPSTRr = null;

            String CPSTR = challenge + "-" + Passwd;
            byte[] CPSTRb = CPSTR.getBytes("UTF-16LE");
          //  System.out.println("TEST" + newStr);
            //	System.out.println(CPSTR);

            //System.out.println(MD5(CPSTR)+"MD5");
          //  System.out.println("1234567z-9e224a41eeefa284df7bb0f26c2913e2");
            //System.out.println(mde5(CPSTR)+"mde5");
            //	CPSTR= new String(CPSTR.getBytes());

            //	String converted =  new String(CPSTR.getBytes("UTF-16LE"), "ISO-8859-1");
          // System.out.println(CPSTR);

            String RESPONSE = challenge + "-" + getMD5Digest(CPSTRb);
            System.out.println(RESPONSE);

            String body = "login:command/response=" + RESPONSE + "&getpage=../html/login_sid.xml";

            URL url = new URL("http://"+Fritzscanner.fritz+"/cgi-bin/webcm");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(body.length()));

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();


            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            for (String line; (line = reader.readLine()) != null;) {

                System.out.println(line);
                if (line.contains("SID")) {
                    Pattern MY_PATTERN = Pattern.compile(".*<SID>(.*)</SID>.*");
                    Matcher m = MY_PATTERN.matcher(line);
                    while (m.find()) {
                        challenge = m.group(1).trim();
                        bool = true;
                        System.out.println("SID" + challenge);

                    }


                }
            }

            writer.close();
            reader.close();


        } catch (Exception e) {
            System.out.println("Fehler");
        }

    }

    private String getMD5Digest(byte[] buffer) {
        String resultHash = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] result = new byte[md5.getDigestLength()];
            md5.reset();
            md5.update(buffer);
            result = md5.digest();

            StringBuffer buf = new StringBuffer(result.length * 2);

            for (int i = 0; i < result.length; i++) {
                int intVal = result[i] & 0xff;
                if (intVal < 0x10) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(intVal));
            }

            resultHash = buf.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return resultHash;
    }
}