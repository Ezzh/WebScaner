package com.example.javawebscannerfx;


import java.io.*;
import java.net.UnknownHostException;


public class Fuzz {
    String Url;
    String mode;
    String wordlistPath;
    File wordlistFile;
    FileReader fr;
    BufferedReader reader;


    String settings(String url, String wordlist, String mode) throws IOException {
        this.mode = mode;
        if (!(mode.equals("dir")) && !(mode.equals("subdomain"))){
            return "Выбран несуществующий режим";
        }
        this.Url = url;
        this.wordlistPath = wordlist;
        this.wordlistFile = new File(wordlistPath);
        if (!wordlistFile.isFile()){
            return wordlistFile.getName() + " not a file";
        }
        fr = new FileReader(wordlistFile);
        reader = new BufferedReader(fr);
        return "";

    }

    public void dirScan() throws IOException {
        //---------Getting url and path to wordlist---------

        HttpRequest request = new HttpRequest(Url);

        String line = "";
        while(true){
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            request.directory = line;
            request.dir();
            if (!request.response.contains("404")){
                System.out.println(request.response);
            }
        }
    }

    void subdomainScan() throws IOException {
        HttpRequest request = new HttpRequest(Url);

        String line = "";
        while(true){
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            request.domain = line;
            try {
                request.sub();
            } catch (UnknownHostException e) {
                System.out.println("** server can't find " + line + "." + Url + ": NXDOMAIN");
            }
        }
    }
}