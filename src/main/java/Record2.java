import org.json.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Record2 {

    private String settlementDate;
    private String region;
    private String regionId;
    private Double rrp;
    private Double totalDemand;
    private String periodType;
    private Double netInterchange;
    private Double scheduledGeneration;
    private Double semiScheduledGeneration;

    public static SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static SimpleDateFormat outer = new SimpleDateFormat("MM-dd/HH:mm");

    public Record2(JSONObject data) throws ParseException {
            settlementDate = outer.format(parser.parse(data.getString("SETTLEMENTDATE")));
            region = data.getString("REGION");
            regionId = data.getString("REGIONID");
            rrp = data.getDouble("RRP");
            totalDemand = data.getDouble("TOTALDEMAND");
            netInterchange = data.getDouble("NETINTERCHANGE");
            scheduledGeneration = data.getDouble("SCHEDULEDGENERATION");
            semiScheduledGeneration = data.getDouble("SCHEDULEDGENERATION");
            periodType = data.getString("PERIODTYPE");
    }

    public String toJSON() {
        return "{" +
                "\"settlementDate\":\"" + settlementDate + "\"" +
                ", \"spotPrice\":" + rrp +
//                ", \"totalDemand\":" + totalDemand +
//                ", \"scheduledGeneration\":" + scheduledGeneration +
//                ", \"semiScheduledGeneration\":" + semiScheduledGeneration +
//                ", \"netInterchange\":" + netInterchange +
                ", \"periodType\":\"" + periodType + "\"" +
                '}';
    }

    @Override
    public String toString() {
        return "Record2{" +
                "settlementDate=" + settlementDate +
                ", region='" + region + '\'' +
                ", regionId='" + regionId + '\'' +
                ", rrp=" + rrp +
                ", totalDemand=" + totalDemand +
                ", periodType='" + periodType + '\'' +
                ", netInterchange=" + netInterchange +
                ", scheduledGeneration=" + scheduledGeneration +
                ", semiScheduledGeneration=" + semiScheduledGeneration +
                '}';
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public String getRegion() {
        return region;
    }

    public String getRegionId() {
        return regionId;
    }

    public Double getRrp() {
        return rrp;
    }

    public Double getTotalDemand() {
        return totalDemand;
    }

    public String getPeriodType() {
        return periodType;
    }

    public Double getNetInterchange() {
        return netInterchange;
    }

    public Double getScheduledGeneration() {
        return scheduledGeneration;
    }

    public Double getSemiScheduledGeneration() {
        return semiScheduledGeneration;
    }




}
