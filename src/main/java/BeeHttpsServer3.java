import com.squareup.okhttp.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BeeHttpsServer3 {

    private static Map<String,PassiveExpiringMap<String, Record2>> regionMap = new HashMap<>();

    public static void main(String[] args) throws Exception {

        BeeHttpsServer3 beeServer = new BeeHttpsServer3();
        beeServer.start();
    }

    public void start() throws Exception {
        Timer timer = new Timer();
        timer.schedule(new Updater(), 0, TimeUnit.MINUTES.toMillis(5));

        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(new FileInputStream("bee.pkcs12"), "F!ndm3".toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, "F!ndm3".toCharArray());
        KeyManager[] kms = kmf.getKeyManagers();

        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        trustStore.load(new FileInputStream("bee.pkcs12"), "F!ndm3".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        TrustManager[] tms = tmf.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kms, tms, new SecureRandom());
        HttpsConfigurator configurator = new HttpsConfigurator(sslContext);

        HttpsServer server = HttpsServer.create(new InetSocketAddress(8000), 0);
        server.setHttpsConfigurator(configurator);
        server.createContext("/current", new Stats());
        server.createContext("/ping", new Ping());
        server.setExecutor(null);
        server.start();
    }

    class Updater extends TimerTask {

        public void run() {
            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody body = RequestBody.create(MediaType.parse("JSON"), "{timeScale: [\"30MIN\"]}");
                Request request = new Request.Builder()
                        .url("https://www.aemo.com.au/aemo/apps/api/report/5MIN")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String data =   response.body().string();
                JSONArray arr = new JSONObject(data).getJSONArray("5MIN");
                for (int i = 0; i < arr.length(); i++)
                {
                    Record2 r = new Record2(arr.getJSONObject(i));
                    if(!regionMap.containsKey(r.getRegion())){
                        regionMap.put(r.getRegion(), new PassiveExpiringMap<>(TimeUnit.DAYS.toMillis(2)));
                    }
                    regionMap.get(r.getRegion()).put(r.getSettlementDate(), r);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    static class Stats implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response;
            t.getResponseHeaders().add("Content-Type", "application/json");
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            Date roundedTime = new Date(((new Date().getTime()/60000) / 30) * 30 * 60000);

            Record2 record = regionMap.get("NSW1").get(Record2.outer.format(roundedTime));
            if(record == null){
                response = "{\"spotPrice\":\"Nothing found!\"}";
            } else {
                response = record.toJSON();
            }

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class Ping implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "pong";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}