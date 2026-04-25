package algs;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Hello Algorithms");

        RangeAlg nt = new RangeAlg(C.SAM, 36000.0, 10.0, 4.0,0.0,0.5);
        RangeAlg at = new RangeAlg(C.SAM, 36000.0, 10.0, 4.0, 1.0,0.5);
        RangeAlg dt = new RangeAlg(C.SAM, 36000.0, 10.0, 4.0, -1.0,0.5);
        nt.start();
        nt.join();
        System.out.println("\n****************************************************************\n");
        at.start();
        at.join();
        System.out.println("\n****************************************************************\n");
        dt.start();
        dt.join();

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