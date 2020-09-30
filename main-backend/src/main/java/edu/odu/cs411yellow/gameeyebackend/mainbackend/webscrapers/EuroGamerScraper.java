package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.UUID;

public class EuroGamerScraper implements WebScraper {
    private static String rssFeed = "https://www.eurogamer.net/?format=rss";
    private List<Article> articles;
    private DateFormat format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");


    public EuroGamerScraper() {
        articles = new ArrayList<>();
        scrape();
    }

    /**
     * Constructor
     * @param articles from feed
     */
    public EuroGamerScraper(List<Article> articles) {
        this.articles = articles;
    }

    /**
     * Initiate the scrape
     */
    @Override
    public void scrape() {

        try {
            Document feed = Jsoup.connect(rssFeed).get();

            Date buildDate = format.parse(feed.selectFirst("lastBuildDate").text());
            NewsWebsite Eurogamer = new NewsWebsite(UUID.randomUUID().toString(),"Eurogamer", null,
                    "https://www.eurogamer.net/", rssFeed, buildDate, buildDate);

            Elements items = feed.select("item");

            for (var i : items){

                Article toAdd = createArticle(i,Eurogamer);

                //Prevent Duplicate Articles
                boolean duplicateExists = false;
                for (Article a : articles) {
                    if (toAdd.getTitle().contentEquals(a.getTitle()))
                        duplicateExists = true;
                }
                if (!duplicateExists)
                    articles.add(toAdd);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

    public Article createArticle(Element i, NewsWebsite site) throws ParseException {
        String title = i.select("title").text();

        String url = i.select("link").text();

        //parse date
        String pubDate = i.select("pubDate").text();
        Date publicationDate = format.parse(pubDate);

        //parse snippet
        Document body = Jsoup.parse(i.select("description").text());
        Elements paragraph = body.select("p");
        String snippet = paragraph.text();
        if (snippet.length() > 255)
            snippet = snippet.substring(0,255);

        //Create a Unique ID
        String id = UUID.randomUUID().toString();
        return new Article(id, title, url, site,
                new Image(id, ".jpg",null), snippet, publicationDate, publicationDate, 0);

    }

    /**
     * Retrieve articles
     * @return list of articles
     */
    @Override
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Retrieve article given index
     * @param index Index pertaining to an article
     * @return article given an index
     */
    @Override
    public Article getArticle(int index) {
        return articles.get(index);
    }

    /**
     * Output to JSON format
     * @return JSON
     */
    @Override
    public String toString() {
        Gson json = new GsonBuilder().setPrettyPrinting().create();
        return json.toJson(this.articles);
    }

    //TODO Categorize Each game article
}
