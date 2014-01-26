package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
//import models.json.datamodel.GetNgramResponse;
import models.json.datamodel.GetNgramResponse;
import models.json.datamodel.ListDate;
import models.json.datamodel.PartiesNgrams;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by michalsiemionczyk on 06/01/14.
 */
public class Ngrams extends Controller {


    /**
     * request should go like:
     * {
     * "dateFrom"    : 2012-06-02,
     * "dateTo"      : 2012-08-23,
     * "ngram"      : "some ngram value"
     * }
     * <p/>
     * and bitches should be like happy.
     * <p/>
     * response:
     * {
     * "ngram1" :   "some",
     * "ngram2" :   "ngram",
     * "ngram3" :   "value"
     * }
     *
     * @return
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result searches() {
        JsonNode json = request().body().asJson();
        String ngramValue = json.findPath("ngram").textValue();
        String[] ngrams = ngramValue.split(" ");
        ObjectNode result = Json.newObject();
        int i = 1;
        for (String ngram : ngrams) {

            Logger.info(ngram);
            result.put("" + i, ngram);
            i++;
        }
        return ok(result);
    }

    static String[] partyNames = new String[]{"PIS", "PO", "SLD"};

    /**
     * Should return
     * {
     *
     *
     "partyA":{
         "2012-05-02": 5,
         "2012-05-03": 3,
         "2012-05-04": 9,
         "2012-05-05": 11
     },
     "partyB":{
         "2012-05-02": 11,
         "2012-05-03": 9,
         "2012-05-04": 3,
         "2012-05-05": 5
     }
     };

     *
     {
         partiesNgrams: [

             {
                 "partyA":[
                     {"2012-05-02": 5},
                     {"2012-05-03": 3},
                     {"2012-05-04": 9},
                     {"2012-05-05": 11}
                    ]
             },
             {
                "partyB":[
                     {"2012-05-02": 11},
                     {"2012-05-03": 9},
                     {"2012-05-04": 3},
                     {"2012-05-05": 5}
                    ]
             }
            ]
     }
     *
     * @param ngramName string but with spaces*/
    public static Result getNgram( String ngramName) {
        Logger.info("get Ngram:" + ngramName);

        SimpleDateFormat sf = new SimpleDateFormat( "yyyy-MM-dd");

        GetNgramResponse response = new GetNgramResponse();
        response.partiesNgramses = new ArrayList<PartiesNgrams>();

        for ( String partyN : partyNames){

            PartiesNgrams pNgrams = new PartiesNgrams();
            pNgrams.listDates = new ArrayList<ListDate>();
            pNgrams.name = partyN;

            Date[] datesForParty = generateDays( new Date(), 1, 10);
            for ( Date date : datesForParty){
                ListDate lDate = new ListDate();
                lDate.date = sf.format( date );
                lDate.count = getRandomInt( 15 );

                pNgrams.listDates.add( lDate );
            }

            response.partiesNgramses.add( pNgrams);

        }

        Logger.debug( Json.toJson( response ).toString() );
        return ok(Json.toJson(response));
    }

    private static int getRandomInt( int max){
        Random generator = new Random();
        return  generator.nextInt( max ) + 1;
    }


    private static Date[] generateDays( Date dateFrom, int daysInterval, int nrOccurences){
        Date[] dates = new Date[ nrOccurences ];

        GregorianCalendar calStart = new GregorianCalendar( );
        calStart.setTime( dateFrom );
        int i = 0;
        while ( i < nrOccurences){
            dates[i] = calStart.getTime();

            calStart.add( Calendar.DAY_OF_YEAR, daysInterval);
            i++;
        }

        return dates;
    }
}
