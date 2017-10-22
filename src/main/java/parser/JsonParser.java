package parser;

import org.hibernate.HibernateException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import parser.entities.Category;
import parser.entities.Page;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonParser {
    public static void main (String[] args) throws IOException {

        System.out.println("Parsing categories...");
        //List<Category> categories = parseCategoriesToDatabase();
        System.out.println("Finished parsing categories!");

        System.out.println("Saving categories to file...");
        //WikiDB.getInstance().saveAllCategoriesToFile("D:\\BigData\\Categories.txt");
        System.out.println("Categories are saved to file Categories.txt");

        System.out.println("Parsing pages...");
        //parsePagesToDatabase(categories);
        System.out.println("Finished parsing pages!");

        System.out.println("Saving pages to file...");
        //WikiDB.getInstance().saveAllPagesToFile("D:\\BigData\\Pages.txt");
        System.out.println("Pages are saved to file Pages.txt");

        System.out.println("Saving all data to file...");
        WikiDB.getInstance().saveAllDataToFile("D:\\BigData\\AllData.txt");
        System.out.println("All data is saved to file AllData.txt");

        System.out.println("FINISH");

        return;
    }

    public static List<Category> parseCategoriesToDatabase() throws IOException {
        List<Category> categoriesResult = new ArrayList<Category>();
        int counter=1;
        for (char letter = 'A'; letter <= 'Z'; ++letter) {
            String url = "https://en.wikipedia.org/w/api.php?action=query&acprop=size&format=json&list=allcategories&aclimit=max&acprefix=" + letter;
            JSONObject json = readJsonFromUrl(url);

            JSONObject query = new JSONObject(json.get("query").toString());

            JSONArray categories = (JSONArray) query.get("allcategories");
            Iterator<Object> iterator = categories.iterator();

            while (iterator.hasNext()) {
                System.out.println(counter++);
                JSONObject categoryJSON = (JSONObject) iterator.next();

                Category category = new Category(categoryJSON.get("*").toString(), Integer.parseInt(categoryJSON.get("files").toString()), Integer.parseInt(categoryJSON.get("pages").toString()));
                categoriesResult.add(category);
                WikiDB.getInstance().addCategory(category);
            }
        }

        return categoriesResult;
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

    public static void parsePagesToDatabase(List<Category> categories) throws IOException {
        int counter = 1;

        for (Category category : categories) {
            System.out.println("Category - " + counter++);
            String categoryName = category.getCategoryName().replaceAll(" ", "%20");
            categoryName = categoryName.replaceAll("&", "%26");
            String url = "https://en.wikipedia.org/w/api.php?action=query&list=categorymembers&format=json&cmlimit=max&cmtitle=Category:" + categoryName;
            JSONObject json = readJsonFromUrl(url);

            JSONObject query = new JSONObject(json.get("query").toString());

            JSONArray pages = (JSONArray) query.get("categorymembers");
            Iterator<Object> iterator = pages.iterator();

            while (iterator.hasNext()) {

                JSONObject pageJSON = (JSONObject) iterator.next();
                if (!pageJSON.get("title").toString().contains("Category:")) {
                    Page page = new Page(pageJSON.get("title").toString(), category);
                    try {
                        WikiDB.getInstance().addPage(page);
                    } catch (HibernateException ex) {
                        continue;
                    }
                }
            }
        }
    }
}
