package dataReader.python;

public class BFCMain {

    public static void main(String[] args) throws Exception {

        BFCFolderSequentialReader reader =
                new BFCFolderSequentialReader("C:\\Users\\NoteBook\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\python\\batch");
        BFCRecord record;

        while ((record = reader.next()) != null) {
            System.out.println(record);
        }
        System.out.println("✅ All files processed.");
    }

}
