import com.example.helloworld.factory.NgramProvider;
import org.sejmngram.database.fetcher.json.datamodel.ListDate;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.PartiesNgrams;

/**
 * Created by michalsiemionczyk on 15/03/14.
 */
public class RestServer {

    /** These are just dates, dont mind them */
    public static void main(String[] args){



        NgramProvider nF = new NgramProvider();

        NgramResponse respo = nF.generateDefaultNgramResponse("nie");

        for ( PartiesNgrams pNgrams :  respo.getPartiesNgrams()){
            System.out.println( pNgrams.getName());
            for ( ListDate lDate :pNgrams.getListDates()) {
                System.out.println(lDate.getCount());
            }
        }



    }
}
