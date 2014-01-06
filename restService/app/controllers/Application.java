package controllers;

import play.libs.F;
import play.mvc.*;

import java.util.Map;


public class Application extends Controller {


    public static final String URL_API_PREFIX = "api";


    public static Result index() {

        Result ok = ok("Welcome to out API, you should call api methods with /api/<method_name>");
        return ok;

    }


/*    *//**
     *
     * {
     *     keyParam1: valueParam1,
     *     keyParam2: valueParam2,
     * }
     *
     * @return
     *//*
    public static Result ngrams(){
        Map<String, String[]> params = request().queryString();

        String returnString = "";
        for ( String key :  params.keySet()){
            returnString += key + " -> " + params.get( key)[0] + "\n";

        }

        return ok (returnString);

    }*/

    private static Integer intensiveComputation(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            return 100 * 100 * 100 * 100;
        }

    }


    public static Result testMethod(){
        return ok ("nice test");
    }

}
