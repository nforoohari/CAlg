package mainPackage;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        //Gathering

//        DataGather btc = new DataGather(Currency.Bitcoin);
//        btc.gather(Interval.OneDay, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        btc.gather(Interval.OneHour, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        btc.gather(Interval.OneMinute, "2025-01-01 00:00:00", "2025-01-03 00:00:00");
//        btc.gather(Interval.OneSecond, "2025-01-01 00:00:00", "2025-01-01 01:00:00");
//
//        DataGather eth = new DataGather(Currency.Ethereum);
//        eth.gather(Interval.OneDay, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        eth.gather(Interval.OneHour, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        eth.gather(Interval.OneMinute, "2025-01-01 00:00:00", "2025-01-03 00:00:00");
//        eth.gather(Interval.OneSecond, "2025-01-01 00:00:00", "2025-01-01 01:00:00");
//
//        DataGather ton = new DataGather(Currency.Toncoin);
//        ton.gather(Interval.OneDay, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        ton.gather(Interval.OneHour, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        ton.gather(Interval.OneMinute, "2025-01-01 00:00:00", "2025-01-03 00:00:00");
//        ton.gather(Interval.OneSecond, "2025-01-01 00:00:00", "2025-01-01 01:00:00");
//
//        DataGather sol = new DataGather(Currency.Solana);
//        sol.gather(Interval.OneDay, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        sol.gather(Interval.OneHour, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        sol.gather(Interval.OneMinute, "2025-01-01 00:00:00", "2025-01-03 00:00:00");
//        sol.gather(Interval.OneSecond, "2025-01-01 00:00:00", "2025-01-01 01:00:00");

//        DataGather link = new DataGather(Currency.Chainlink);
//        link.gather(Interval.OneDay, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        link.gather(Interval.OneHour, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        link.gather(Interval.OneMinute, "2025-01-01 00:00:00", "2025-01-03 00:00:00");
//        link.gather(Interval.OneSecond, "2025-01-01 00:00:00", "2025-01-01 01:00:00");

//        DataGather ada = new DataGather(Currency.Cardano);
//        ada.gather(Interval.OneDay, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        ada.gather(Interval.OneHour, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        ada.gather(Interval.OneMinute, "2025-01-01 00:00:00", "2025-01-03 00:00:00");
//        ada.gather(Interval.OneSecond, "2025-01-01 00:00:00", "2025-01-01 01:00:00");
//
//        DataGather paxg = new DataGather(Currency.XAUT);
//        paxg.gather(Interval.OneDay, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        paxg.gather(Interval.OneHour, "2025-01-01 00:00:00", "2025-02-01 00:00:00");
//        paxg.gather(Interval.OneMinute, "2025-01-01 00:00:00", "2025-01-03 00:00:00");
//        paxg.gather(Interval.OneSecond, "2025-01-01 00:00:00", "2025-01-01 01:00:00");
//
//        Reading
//        ExcelFolderReader reader =
//                new ExcelFolderReader(Currency.Bitcoin, Interval.OneSecond, "C:\\Users\\NoteBook\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\api");
//        Record record;
//        DBInserter db = new DBInserter(Interval.OneSecond);
//
//        while ((record = reader.next()) != null) {
//            db.batchAddAndInsert(record);
//            System.out.println(record);
//        }
//        db.batchAddAndInsert(record);
//
//        System.out.println("✅ All files processed.");

        //Showing

        List<Record> candles =
                DBLoader.load(Interval.OneSecond, Currency.Bitcoin, "2025-01-01 00:00:00", "2025-01-01 01:00:00");
        TradingViewChartOffline.show(candles);

    }
}
