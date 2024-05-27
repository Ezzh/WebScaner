package com.example.javawebscannerfx;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class HttpRequest {
    public HttpRequest(String url) throws FileNotFoundException {
        this.Url = url;
    }

    HttpClient httpClient = HttpClientBuilder.create().build();
    HttpGet httpGet;
    HttpOptions httpOptions;
    HttpResponse httpResponse;
    String response;
    String Url;
    String finalUrl;
    String directory;
    String parameter;
    String domain;
    Scanner console = new Scanner(System.in);
    String userAnswer;

    public void dir() throws IOException {
        finalUrl = "http://" + Url + "/" + directory;
        httpGet = new HttpGet(finalUrl);
        //System.out.println(finalUrl);
        httpResponse = httpClient.execute(httpGet);
        if (httpResponse == null){
            response = "HTTP/1.1 404 Not Found";
        }else {
            response = String.valueOf(httpResponse.getStatusLine());
        }
        //System.out.println("response: "+response);
        httpGet.releaseConnection();
    }

    public void sub() throws IOException {
        finalUrl = "http://" + domain + "." + Url;
        System.out.println(finalUrl);
        httpGet = new HttpGet(finalUrl);
        httpResponse = httpClient.execute(httpGet);
        if (httpResponse == null){
            response = "HTTP/1.1 404 Not Found";
        }else {
            response = String.valueOf(httpResponse.getStatusLine());
        }
        System.out.println("response: "+response);
        httpGet.releaseConnection();

    }

    public String OPTIONSrequest() throws IOException {
        finalUrl = "http://" + Url;
        httpOptions = new HttpOptions(finalUrl);
        httpResponse = httpClient.execute(httpOptions);
        httpOptions.releaseConnection();
        return "Allowed methods: " + httpOptions.getAllowedMethods(httpResponse) + "\n-------------------------------------------------------------------------------------------------\n";
    }

    public String getAllHeaders() throws IOException {
        String HeadersOutput = "HEADERS:\n";
        finalUrl = "http://" + Url;
        httpGet = new HttpGet(finalUrl);
        httpResponse = httpClient.execute(httpGet);

        System.out.println("Printing Response Header...\n");

        Header[] headers = httpResponse.getAllHeaders();
        for (Header header : headers) {
            HeadersOutput = HeadersOutput + header.getName()
                    + " : " + header.getValue() + "\n";
        }

        HeadersOutput = HeadersOutput + "\n" + "-------------------------------------------------------------------------------------------------\nImportant and interesting headers:" + "\n";
        /*Important headers check*/

        try {
            String server = httpResponse.getFirstHeader("Server").getValue();
            HeadersOutput = HeadersOutput + "Server - " + server + "\n";
            HeadersOutput = HeadersOutput + "CVE found: https://cve.mitre.org/cgi-bin/cvekey.cgi?keyword=" + server + "\n\n";
        } catch (NullPointerException e) {
            HeadersOutput = HeadersOutput + "Key 'Server' is not found!" + "\n\n";
        }

        try {
            String CSP = httpResponse.getFirstHeader("Content-Security-Policy").getValue();
            HeadersOutput = HeadersOutput + "Content-Security-Policy - " + CSP + "\n\n";
        } catch (NullPointerException e) {
            HeadersOutput = HeadersOutput + "Key 'CSP' is not found!" + "\n\n";
        }

        try {
            String XPB = httpResponse.getFirstHeader("x-powered-by").getValue();
            HeadersOutput = HeadersOutput + "x-powered-by - " + XPB + "\n";
            HeadersOutput = HeadersOutput + "CVE found: https://cve.mitre.org/cgi-bin/cvekey.cgi?keyword=" + XPB + "\n\n";
        } catch (NullPointerException e) {
            HeadersOutput = HeadersOutput + "Key 'x-powered-by' is not found!" + "\n\n";
        }

        try {
            String XBS = httpResponse.getFirstHeader("x-backend-server").getValue();
            HeadersOutput = HeadersOutput + "x-backend-server - " + XBS + "\n\n";
        } catch (NullPointerException e) {
            HeadersOutput = HeadersOutput + "Key 'x-backend-server' is not found!" + "\n\n";
        }

        try {
            String XFO = httpResponse.getFirstHeader("X-Frame-Options").getValue();
            HeadersOutput = HeadersOutput + "X-Frame-Options - " + XFO + "\n\n";
        } catch (NullPointerException e) {
            HeadersOutput = HeadersOutput + "The anti-clickjacking X-Frame-Options header is not present" + "\n";
        }


        HeadersOutput = HeadersOutput + "Done\n-------------------------------------------------------------------------------------------------\n";
        httpGet.releaseConnection();

        /*String body = EntityUtils.toString(httpResponse.getEntity());
        System.out.println(body);*/
        return HeadersOutput;

    }

    public String getHtml() throws IOException {
        finalUrl = "http://" + Url + "/" + directory;
        httpGet = new HttpGet(finalUrl);
        httpResponse = httpClient.execute(httpGet);
        String body = EntityUtils.toString(httpResponse.getEntity());
        httpGet.releaseConnection();

        return body;
    }

    public String requestParam() throws IOException {
        finalUrl ="http://" + Url.replace("payload", parameter);
        //System.out.println(finalUrl);
        httpGet = new HttpGet(finalUrl);
        httpResponse = httpClient.execute(httpGet);
        String body = EntityUtils.toString(httpResponse.getEntity());
        httpGet.releaseConnection();

        return body;
    }

}
