package com.alenut.mpi.main;

//import org.json.JSONArray;

import org.json.simple.JSONArray;
import paralleldots.ParallelDots;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TestMain {
    public static void main(String[] args) throws Exception {

        // Keywords generation using Parallel DOCS API that uses Neural Language Processing
        ParallelDots pd = new ParallelDots("9EiarD6fAQkDPHZpPUnFLDnRySwOtKMEH3NUATr851s");
        String text = "Some text";
        String keywords = pd.keywords(text);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(keywords);
        JSONArray jsonArray = (JSONArray) json.get("keywords");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            String keyword = jsonObject.get("keyword").toString();
        }

    }
}
