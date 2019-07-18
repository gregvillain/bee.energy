
import com.squareup.okhttp.*;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args){

        Map<String,PassiveExpiringMap<String, Record2>> regionMap = new HashMap<>();
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Record2 r: regionMap.get("SA1").values()){
            System.out.println(r);
        }
    }

}
