package algs;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Hello Algorithms");

//        RangeAlg nt = new RangeAlg(C.SAM, 70000.0, 66000.0,10.0, 2.0,  0.0,0.5);
//        RangeAlg at = new RangeAlg(C.SAM, 70000.0, 66000.0,10.0, 2.0,  1.0,0.5);
//        RangeAlg dt = new RangeAlg(C.SAM, 70000.0, 66000.0,10.0, 2.0, -1.0,0.5);
//        FloorAlg nt = new FloorAlg(C.SAM, 36000.0, 34200.0,10.0, 5.0,  0.0,0.5);
//        FloorAlg at = new FloorAlg(C.SAM, 36000.0, 34200.0,10.0, 5.0,  1.0,0.5);
//        FloorAlg dt = new FloorAlg(C.SAM, 36000.0, 34200.0,10.0, 5.0, -1.0,0.5);
//        CeilingAlg nt = new CeilingAlg(C.SAM, 44000.0, 47000.0,10.0, 5.0,  0.0,0.5);
//        CeilingAlg at = new CeilingAlg(C.SAM, 44000.0, 47000.0,10.0, 5.0,  1.0,0.5);
//        CeilingAlg dt = new CeilingAlg(C.SAM, 44000.0, 47000.0,10.0, 5.0, -1.0,0.5);
//        nt.start();
//        nt.join();
//        System.out.println("\n****************************************************************\n");
//        at.start();
//        at.join();
//        System.out.println("\n****************************************************************\n");
//        dt.start();
//        dt.join();

//        Thread.sleep(1000);
//        t.stopThread();

        try {
            CExcelReader reader = new CExcelReader(C.ETH,"C:\\Users\\NoteBook\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\eth_usdt_last_2years_seconds.xlsx");

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