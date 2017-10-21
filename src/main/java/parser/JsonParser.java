package parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import parser.entities.Category;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;

public class JsonParser {
    public static void main (String[] args) throws IOException {
        File myOutput = new File("D:\\output.json");

        try (FileWriter file = new FileWriter(myOutput)) {
            for (char letter = 'A'; letter <= 'Z'; ++letter) {
                String url = "https://en.wikipedia.org/w/api.php?action=query&acprop=size&format=json&list=allcategories&aclimit=max&acprefix=" + letter;
                JSONObject json = readJsonFromUrl(url);

                if(letter == 'A')
                System.out.println(json.toString());
               // parseCategories(json);
                file.append(json.toString());
            }
        }

    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }

            JSONObject json = new JSONObject(sb.toString());
            return json;
        } finally {
            is.close();
        }
    }

    static void parseCategories(JSONObject jsonObject) {

        JSONArray names = (JSONArray) jsonObject.get("*");
        JSONArray files = (JSONArray) jsonObject.get("files");
        JSONArray pages = (JSONArray) jsonObject.get("pages");


        Iterator<Object> iterator1 = names.iterator();
        Iterator<Object> iterator2 = files.iterator();
        Iterator<Object> iterator3 = pages.iterator();

        while (iterator1.hasNext() && iterator2.hasNext() && iterator3.hasNext()) {
            Category category = new Category(iterator1.next().toString(), Integer.parseInt(iterator2.next().toString()), Integer.parseInt(iterator3.next().toString()));
            WikiDB.getInstance().addCategory(category);
        }
    }
}
