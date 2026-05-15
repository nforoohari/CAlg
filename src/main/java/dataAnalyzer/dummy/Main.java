package dataAnalyzer.dummy;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Hello Algorithms");

//        RangeCryptoAlg nt = new RangeCryptoAlg(C.SAM, 36000.0, 34200.0,10.0, 2.0,  0.0,0.5);
//        RangeCryptoAlg at = new RangeCryptoAlg(C.SAM, 36000.0, 34200.0,10.0, 2.0,  1.0,0.5);
//        RangeCryptoAlg dt = new RangeCryptoAlg(C.SAM, 36000.0, 34200.0,10.0, 2.0, -1.0,0.5);
//        FloorCryptoAlg nt = new FloorCryptoAlg(C.SAM, 36000.0, 34200.0,10.0, 5.0,  0.0,0.5);
//        FloorCryptoAlg at = new FloorCryptoAlg(C.SAM, 36000.0, 34200.0,10.0, 5.0,  1.0,0.5);
//        FloorCryptoAlg dt = new FloorCryptoAlg(C.SAM, 36000.0, 34200.0,10.0, 5.0, -1.0,0.5);
//        CeilingCryptoAlg nt = new CeilingCryptoAlg(C.SAM, 44000.0, 47000.0,10.0, 5.0,  0.0,0.5);
//        CeilingCryptoAlg at = new CeilingCryptoAlg(C.SAM, 44000.0, 47000.0,10.0, 5.0,  1.0,0.5);
//        CeilingCryptoAlg dt = new CeilingCryptoAlg(C.SAM, 44000.0, 47000.0,10.0, 5.0, -1.0,0.5);
//        nt.start();
//        nt.join();
//        System.out.println("\n****************************************************************\n");
//        at.start();
//        at.join();
//        System.out.println("\n****************************************************************\n");
//        dt.start();
//        dt.join();


        try {
            CExcelReader reader = new CExcelReader(C.ETH, "C:\\Users\\NoteBook\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\dummy\\btc_usdt_dummy_year.xlsx");

            CRecord rec;
            while ((rec = reader.getNext()) != null) {
                System.out.println(rec);
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}