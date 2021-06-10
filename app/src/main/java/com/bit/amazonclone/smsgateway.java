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
        String twar=getURL()+"httpapi/send?username=kalpitjangid95@gmail.com&password=k7726030493&sender_id=PROMOTIONAL&route=P&phonenumber="+customerPhoneNumber+"&message=PROMOTIONAL";
        return twar;
    }

    private String getURL() {

        return "http://smsc.biz/";
    }
}




