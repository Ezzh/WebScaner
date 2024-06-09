package com.example.javawebscannerfx;

import java.io.IOException;
import java.net.URLEncoder;

public class XssScan {
    public XssScan(String Url) {this.Url = Url;}

    String Url;
    String startPayload = "TestStringPayload";
    String payload = "pl%22pl%3Cpl%3Epl'";
    String page;
    boolean payloadInAttribute;
    String nonFilterableCharacters = "";
    String[] xssTagsPatterns = {">)) pl<script>", "\"\"pl>pl>))))", "\">)) pl<scr<script>ipt>", "pl\"'pl\"pl\"''pl>>pl<< pl<<>pl>", "))\"==pl<pl>pl>pl<"};
    String[] xssAttributePatterns = {"pl\"", "pl\"\"", "pl'pl\"", "\"pl'pl\"))'"};
    int pointsForXss = 0;
    String Output = "";

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
    }

    String XssScan() throws IOException {
        HttpRequest request = new HttpRequest(Url);
        request.parameter = startPayload;
        page = request.requestParam();

        if (page.contains(startPayload)){
            //Check contains attribute user's input
            if (page.contains("\""+startPayload+"\"")){
                payloadInAttribute = true;
            }

            //Common xss scan
            request.parameter = payload;
            page = request.requestParam();
            check(page);
            Output = "the following characters are not filtered at first glance: " + nonFilterableCharacters + "\n\n";
            nonFilterableCharacters = "";

            if (pointsForXss >= 2){
                Output = Output + "It's seems like you can embed tags!" + "\n\n";
                pointsForXss = 0;
            }

            //Searching xss in attributes
            if (payloadInAttribute){
                for (String payload : xssAttributePatterns) {
                    request.parameter = URLEncoder.encode(payload, "UTF-8");
                    page = request.requestParam();
                    check(page);
                }
                Output = Output + "Scanning attribute revealed that the following characters are not filtered in attribute: " + nonFilterableCharacters + "\n";
                nonFilterableCharacters = "";
            }

            //Searching xss with tags
            for (String payload : xssTagsPatterns) {
                request.parameter = URLEncoder.encode(payload, "UTF-8");
                page = request.requestParam();
                check(page);
                if (page.contains("pl<script>")){
                    Output = Output + "managed to implement tag \"<script>\", XSS possible!" + "\n";
                }
            }
            Output = Output + "Scanning with tags revealed that the following characters are not filtered: " + nonFilterableCharacters + "\n";
            nonFilterableCharacters = "";

        }else {
            Output = Output + "something went wrong and the DOM does not contain user input.\n" +
                    "Ensure that an input tag exists on this page with the provided name attribute and implies the location of the user input on the page" + "\n";
        }

        return Output;
    }
}
