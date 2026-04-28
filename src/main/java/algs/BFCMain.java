package algs;

public class BFCMain {

    public static void main(String[] args) throws Exception {

        BFCFolderSequentialReader reader =
                new BFCFolderSequentialReader("C:\\Users\\n_foroohari\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\batch");

        BFCRecord record;

        while ((record = reader.next()) != null) {
            System.out.println(record);
        }

        System.out.println("✅ All files processed.");
    }

}
