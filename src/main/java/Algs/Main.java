package Algs;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Hello Algorithms");

        Alg11Thread t = new Alg11Thread(CNames.BBB, 36000.0, 9.0, 2000.0);
//        t.buy(CNames.EEE, 1.0, 2.3);
//        t.sell(CNames.TTT, 3.0, 4.5);

        t.start();
        Thread.sleep(180);
        t.stopThread();

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