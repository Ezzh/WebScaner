package com.example.javawebscannerfx;

import java.io.IOException;

public class CommonScan {
    public CommonScan(String Url){
        this.Url = Url;
    }
    String Url;
    String dirPayload = "TestStringPayload";
    String page404Html;
    String nonFilterableCharacters = "";
    int pointsForXss = 0;

    //OUTPUTS
    String OPTIONS;
    String Headers;
    String robots;
    String notFoundPage = "";
    //OUTPUTS
    void commonScan() throws IOException {
        HttpRequest request = new HttpRequest(Url);
        OPTIONS = request.OPTIONSrequest();
        Headers = request.getAllHeaders();
        request.directory = "robots.txt";
        request.dir();
        if (request.response.equals("HTTP/1.1 200 OK")){
            robots = "robots.txt: " + request.finalUrl + "\n\n-------------------------------------------------------------------------------------------------\n";
        }else {
            robots = "robots.txt not found" + "\n\n-------------------------------------------------------------------------------------------------\n";
        }

        request.directory = dirPayload;
        page404Html = request.getHtml();
        //System.out.println(page404Html);
        if (page404Html.contains(dirPayload)){
            notFoundPage = notFoundPage + "404 page contains user input" + "\n";

            dirPayload = "pl%22pl%3Cpl%3Epl'";
            request.directory = dirPayload;
            page404Html = request.getHtml();
            //System.out.println(page404Html);

            check(page404Html);

            dirPayload = "%7B%7B7*7%7D%7D";
            request.directory = dirPayload;
            page404Html = request.getHtml();

            if (page404Html.contains("{{49}}")){
                notFoundPage = notFoundPage + "Possible SSTI!" + "\n";
            }else {
                notFoundPage = notFoundPage + "SSTI doesn't seem possible" + "\n";
            }

        }else {
            notFoundPage = notFoundPage + "404 page doesn't contains user input" + "\n";
        }
    }
    void check(String htmlPage){
        if (htmlPage.contains("pl\"")){
            if (!nonFilterableCharacters.contains("\"")) {
                nonFilterableCharacters += "\" ";
            }
        }
        if (htmlPage.contains("pl<")) {
            if (!nonFilterableCharacters.contains("<")) {
                nonFilterableCharacters += "< ";
            }
            pointsForXss++;
        }
        if (htmlPage.contains("pl>")) {
            if (!nonFilterableCharacters.contains(">")) {
                nonFilterableCharacters += "> ";
            }
            pointsForXss++;
        }
        if (htmlPage.contains("pl'")) {
            if (!nonFilterableCharacters.contains("'")) {
                nonFilterableCharacters += "' ";
            }
        }
        if (pointsForXss >= 2){
            notFoundPage = "XSS Possible!" + "\n";
        }else {
            notFoundPage = notFoundPage + "XSS doesn't seem possible" + "\n";
        }
    }
}
