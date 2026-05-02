package mainPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataGather {

    public DataReceiver dr;

    public DataGather(Crypto  crypto) {

        this.dr = new DataReceiver(crypto);

    }

    public void gather(String interval, String startTime, String endTime) throws IOException, InterruptedException {

        List<GatheringDTO> gatheringDTOS = new ArrayList<>();

        for (GatheringDTO gd : gatheringDTOS){
            dr.receive(gd.interval(), gd.startTime(), gd.endTime());
        }

    }


    public static void main(String[] args) {



    }

}
