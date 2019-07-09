import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {

    private static Map<Long, Record> stats;

    public static void main(String[] args) throws Exception {
        String csvFile = "electricity_price_and_demand.csv";
        stats = new HashMap<>();
        parseCsv(csvFile, stats);

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/current", new Stats());
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

}