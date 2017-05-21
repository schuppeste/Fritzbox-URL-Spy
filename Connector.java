/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fritzscanner;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connector implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println("coninit");
            String challenge = null;
            boolean boole = false;
            FirstConnector fcon = new FirstConnector();
            fcon.run();
            while (!boole) {
                boole = fcon.getboolean();
            }
            challenge = fcon.getChallenge();
            System.out.println("Tabledatataaaaa");
            System.out.println(challenge);

            URL urls = new URL("http://" + Fritzscanner.fritz + "/cgi-bin/capture_notimeout?ifaceorminor=3-17&snaplen=1600&capture=Start&sid=" + challenge);

            HttpURLConnection connections = (HttpURLConnection) urls.openConnection();

            HttpURLConnection urlConn = (HttpURLConnection) urls.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.connect();
            int read = 0;

            InputStream in = httpConn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            int bufSize = 1024;
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[bufSize];
            int x = 0;
            String lasthex = "00";
            while (true) {
                read = bis.read(buffer);
                if (read == -1) {
                    break;
                }

                for (byte b : buffer) {

                    String hex = String.format("%02X ", b).trim();
                    if (hex != "00") {	// System.out.println( "Tabledatat");
                        if (lasthex.concat(hex).matches("0A0D") && sb.toString().contains("Host:")) {
                            Pattern MY_PATTERN = Pattern.compile(
                                    "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)"
                                    + "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov"
                                    + "|mil|biz|info|mobi|name|aero|jobs|museum"
                                    + "|travel|[a-z]{2}))(:[\\d]{1,5})?"
                                    + "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?"
                                    + "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
                                    + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)"
                                    + "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
                                    + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*"
                                    + "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");
                            Matcher m = MY_PATTERN.matcher(sb.toString());
                            while (m.find()) {
                                String ok = m.group();
                                int row = Fritzscanner.tableModel.getRowCount();

                                if (Fritzscanner.tableModel.checkDoubles(new Object[]{getDomainName(ok), 0, 0, 0}) == true) {

                                    System.out.println("CHECK");
                                    Fritzscanner.tableModel.setValueAt(new Object[]{getDomainName(ok), 0, 0, 0}, row, 0);
                                    Fritzscanner.tableModel.fireTableDataChanged();
                                }




                            }
                            sb.delete(0, sb.length());
                        }
                        int i = Integer.parseInt(hex, 16);
                        char c = (char) i;
                        sb.append(c);
                        lasthex = hex;
                        // if(sb.indexOf("0D0A")>0){


                        //  }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getDomainName(String url) {
        String domain = null;
        try {
            URI uri = new URI(url);
            domain = uri.getHost();
        } catch (Exception e) {
        }
        if (domain == null) {
            domain = "www.example.de";
        }

        String temp = domain.startsWith("www.") ? domain.substring(4) : domain;

        return temp;
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
