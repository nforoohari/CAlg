package Algs;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Hello Algorithms");

//        Alg1Thread t = new Alg1Thread(CNames.BBB,3.1F,9F,0.8F);
//        t.buy(CNames.EEE, 1.0F, 2.3F);
//        t.sell(CNames.TTT, 3.0F, 4.5F);
//
//        t.start();
//        Thread.sleep(18000);
//        t.stopThread();

        try {
            BTCExcelReader reader = new BTCExcelReader("C:\\Users\\n_foroohari\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\btc_usdt_sample.xlsx");

            BTCRecord rec;
            while ((rec = reader.getNext()) != null) {
                System.out.println(rec);
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}