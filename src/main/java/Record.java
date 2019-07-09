import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {

    private Date settlementDate;
    private double spotPrice;
    private double scheduledDemand;
    private double scheduledGeneration;
    private double semiScheduledGeneration;
    private double netImport;
    private String type;

    public static SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public Record(String[] lines) throws ParseException {
        settlementDate = parser.parse(lines[0]);
        spotPrice = Double.valueOf(lines[1]);
        scheduledDemand = Double.valueOf(lines[2]);
        scheduledGeneration = Double.valueOf(lines[3]);
        semiScheduledGeneration = Double.valueOf(lines[4]);
        netImport = Double.valueOf(lines[5]);
        type = lines[6];
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public double getSpotPrice() {
        return spotPrice;
    }

    public double getScheduledDemand() {
        return scheduledDemand;
    }

    public double getScheduledGeneration() {
        return scheduledGeneration;
    }

    public double getSemiScheduledGeneration() {
        return semiScheduledGeneration;
    }

    public double getNetImport() {
        return netImport;
    }

    public String getType() {
        return type;
    }

    public String toJSON() {
        return "{" +
                "\"settlementDate\":\"" + settlementDate + "\"" +
                ", \"spotPrice\":" + spotPrice +
                ", \"scheduledDemand\":" + scheduledDemand +
                ", \"scheduledGeneration\":" + scheduledGeneration +
                ", \"semiScheduledGeneration\":" + semiScheduledGeneration +
                ", \"netImport\":" + netImport +
                ", \"type\":\"" + type + "\"" +
                '}';
    }

    @Override
    public String toString() {
        return "Record{" +
                "settlementDate='" + settlementDate + '\'' +
                ", spotPrice=" + spotPrice +
                ", scheduledDemand=" + scheduledDemand +
                ", scheduledGeneration=" + scheduledGeneration +
                ", semiScheduledGeneration=" + semiScheduledGeneration +
                ", netImport=" + netImport +
                ", type='" + type + '\'' +
                '}';
    }
}
