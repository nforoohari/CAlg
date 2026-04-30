package dataAnalyzer.java;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Hello Algorithms");

        try {
            CExcelReader reader = new CExcelReader(C.ETH,"C:\\Users\\NoteBook\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\java\\eth_usdt_last_2years_seconds_utc.xlsx");
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