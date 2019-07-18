import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BeeHttpsServer2 {

    private static Map<Long, Record> stats;

    public static void main(String[] args) throws Exception {
        String csvFile = "electricity_price_and_demand.csv";
        stats = new HashMap<>();
        parseCsv(csvFile, stats);

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

    //TODO: download every half an hour and rename

    public static void parseCsv(String csvFile, Map<Long, Record> stats) {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            //skip first line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(cvsSplitBy);
                SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date date = parser.parse(lines[0]);
                stats.put(date.getTime(), new Record(lines));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Stats implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response;

            t.getResponseHeaders().add("Content-Type", "application/json");
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            Long roundedTime = ((new Date().getTime()/60000) / 30) * 30 * 60000;
            Record record = stats.get(roundedTime);
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