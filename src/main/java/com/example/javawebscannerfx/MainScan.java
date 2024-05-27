package com.example.javawebscannerfx;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Scanner;

public class MainScan {
    public static void main(String[] args) throws IOException {
        Fuzz fuzz = new Fuzz();
        CommonScan common = new CommonScan("");
        //XssScan xssScan = new XssScan();
        Scanner console = new Scanner(System.in);

        System.out.println("Choose mode: fuzz, common, xss");
        String mode = console.nextLine();

        if (mode.equals("fuzz")) {
            //fuzz.settings("");
            if (fuzz.mode.equals("dir")) {
                fuzz.dirScan();
            } else if (fuzz.mode.equals("subdomain")) {
                fuzz.subdomainScan();
            }
        } else if (mode.equals("common")) {
            common.commonScan();
        } else if (mode.equals("xss")) {
            //xssScan.settings();
            //xssScan.XssScan();
        }


        //HttpPost httpPost = new HttpPost(yandexSite);
        //httpPost.setHeader("qwe", "qwe");
        //httpResponse = httpClient.execute(httpPost);
        //body = EntityUtils.toString(httpResponse.getEntity());
        //System.out.println(body);
    }
}