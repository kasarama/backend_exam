/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author magda
 */
public class HttpUtils {

    private static ExecutorService threadPool; 
    private static Gson GSON;

    public static String fetchData(String _url) throws MalformedURLException, IOException {

        URL url = new URL(_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("User-Agent", "server");

        Scanner scan = new Scanner(con.getInputStream());
        String jsonStr = "";
        while (scan.hasNext()) {
            jsonStr += scan.nextLine();
        }
        scan.close();
        return jsonStr;
    }

    public static ExecutorService getThreadPool() {
        if (threadPool == null) {
            threadPool = Executors.newCachedThreadPool();
        }
        return threadPool;
    }

    public static Gson getGSON() {
        if (GSON == null) {
            GSON = new GsonBuilder().setPrettyPrinting().create();
        }
        return GSON;
    }
}
