package com.bit.amazonclone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class smsgateway {

    public String customerUserName,customerPassword,customerPhoneNumber;



    /**
     * @param args
     */
    public static void main(String[] args) {
smsgateway test=new smsgateway();
test.sendMessage();
    }
    void sendMessage() {

        try {

            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager()
                    {
                        @Override
                        public void checkClientTrusted( final X509Certificate[] chain, final String authType ) {
                        }
                        @Override
                        public void checkServerTrusted( final X509Certificate[] chain, final String authType ) {
                        }
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    } };



            final SSLContext sslContext = SSLContext.getInstance( "SSL" );
            sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory( sslContext.getSocketFactory() );
            URL url = new URL(getURLPath());
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getURLPath() {
        String twar=getURL()+"httpapi/send?username=kalpitjangid247@gmail.com&password=7726030493&sender_id=SMSCIN&route=T&phonenumber="+customerPhoneNumber+"&message=Dear%20%2C%20%23"+customerUserName+"%23%20your%20tickets%20are%20confirmed%20for%20%23field2%23%20.%20Date%20-%20%23field3%23%20Time%20-%20%23field4%23%20Seats%20-%20%23field5%23%20Pick%20Up%20code%20-%20%23field6%23%20Please%20collect%20your%20tickets%20from%20MOVIEPLEX%20_Box_Office.";
        return twar;
    }

    private String getURL() {

        return "http://smsc.biz/";
    }
}




