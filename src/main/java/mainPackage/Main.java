package mainPackage;

import java.io.IOException;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws Exception {

        //Gathering

        DataGather btc = new DataGather(Crypto.Bitcoin);
        btc.gather("1d", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        btc.gather("1h", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        btc.gather("1m", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        btc.gather("1s", "2025-01-01 00:00:00", "2025-02-01 00:00:00");

        DataGather eth = new DataGather(Crypto.Ethereum);
        eth.gather("1d", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        eth.gather("1h", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        eth.gather("1m", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        eth.gather("1s", "2025-01-01 00:00:00", "2025-02-01 00:00:00");

        DataGather ton = new DataGather(Crypto.Toncoin);
        ton.gather("1d", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        ton.gather("1h", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        ton.gather("1m", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        ton.gather("1s", "2025-01-01 00:00:00", "2025-02-01 00:00:00");

        DataGather sol = new DataGather(Crypto.Solana);
        sol.gather("1d", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        sol.gather("1h", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        sol.gather("1m", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        sol.gather("1s", "2025-01-01 00:00:00", "2025-02-01 00:00:00");

        DataGather link = new DataGather(Crypto.Chainlink);
        link.gather("1d", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        link.gather("1h", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        link.gather("1m", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        link.gather("1s", "2025-01-01 00:00:00", "2025-02-01 00:00:00");

        DataGather ada = new DataGather(Crypto.Cardano);
        ada.gather("1d", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        ada.gather("1h", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        ada.gather("1m", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        ada.gather("1s", "2025-01-01 00:00:00", "2025-02-01 00:00:00");

        DataGather paxg = new DataGather(Crypto.PAXG);
        paxg.gather("1d", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        paxg.gather("1h", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        paxg.gather("1m", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
        paxg.gather("1s", "2025-01-01 00:00:00", "2025-02-01 00:00:00");

        //Reading
        ExcelFolderReader reader =
                new ExcelFolderReader(Crypto.Bitcoin,"1d","C:\\Users\\NoteBook\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\main");

        CryptoRecord record;
        DBInserter db = new DBInserter("crypto_day");

        while ((record = reader.next()) != null) {
            db.batchAddAndInsert(record);
            System.out.println(record);
        }
        db.batchAddAndInsert(record);

        System.out.println("✅ All files processed.");

    }
}
