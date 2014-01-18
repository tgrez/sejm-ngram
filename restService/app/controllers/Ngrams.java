package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

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


    public static Result list( String list) {
        ObjectNode result = Json.newObject();
        int k = 1;
        for (int i = 0; i < 10; i++) {

            result.put("" + i, list);
            k++;
        }
        return ok(result);
    }
}
