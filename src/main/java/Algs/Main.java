package Algs;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Hello Algorithms");

//        NormalRangeAlg nt = new NormalRangeAlg(CNames.EEE, 36000.0, 10.0, 3.0);
//        AscendingRangeAlg at = new AscendingRangeAlg(CNames.EEE, 36000.0, 10.0, 3.0, 1.0);
//        DescendingRangeAlg dt = new DescendingRangeAlg(CNames.EEE, 36000.0, 10.0, 3.0, 1.0);
        RangeAlg nt = new RangeAlg(CNames.EEE, 36000.0, 10.0, 5.0,0.0,0.5);
        RangeAlg at = new RangeAlg(CNames.EEE, 36000.0, 10.0, 5.0, 1.0,0.5);
        RangeAlg dt = new RangeAlg(CNames.EEE, 36000.0, 10.0, 5.0, -1.0,0.5);
        nt.start();
        nt.join();
        System.out.println("\n****************************************************************\n");
        at.start();
        at.join();
        System.out.println("\n****************************************************************\n");
        dt.start();
//        Thread.sleep(1000);
//        t.stopThread();

//        try {
//            CExcelReader reader = new CExcelReader("C:\\Users\\n_foroohari\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\btc_usdt_year_ohlc.xlsx");
//
//            CRecord rec;
//            while ((rec = reader.getNext()) != null) {
//                System.out.println(rec);
//            }
//
//            reader.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}