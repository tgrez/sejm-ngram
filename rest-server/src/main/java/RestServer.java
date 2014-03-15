import com.example.helloworld.factory.NgramFactory;
import org.sejmngram.database.fetcher.json.datamodel.ListDate;
import org.sejmngram.database.fetcher.json.datamodel.NgramResponse;
import org.sejmngram.database.fetcher.json.datamodel.PartiesNgrams;

/**
 * Created by michalsiemionczyk on 15/03/14.
 */
public class RestServer {

    /** These are just dates, dont mind them */
    public static void main(String[] args){



        NgramFactory nF = new NgramFactory();

        NgramResponse respo = nF.generateDefaultNgramResponse("nie");

        for ( PartiesNgrams pNgrams :  respo.getPartiesNgrams()){
            System.out.println( pNgrams.getName());
            for ( ListDate lDate :pNgrams.getListDates()) {
                System.out.println(lDate.getCount());
            }
        }



    }
}
