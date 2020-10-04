package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;
import java.util.UUID;

@Service ("EuroGamerScrape")
public class EuroGamerScraper implements WebScraper {

    private static final String rssFeed = "https://www.eurogamer.net/?format=rss";
    private List<Article> articles;
    private static final DateFormat format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");

    @Autowired
    private NewsWebsiteRepository siteBuilder;

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

            NewsWebsite Eurogamer = siteBuilder.findByName("Eurogamer");

            Elements items = feed.select("item");

            for (var i : items){

                Article toAdd = createArticle(i,Eurogamer);

                if (!checkDuplicateArticles(toAdd))
                    articles.add(toAdd);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
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

    @Override
    public Boolean checkDuplicateArticles(Article a) {

        for (Article i : articles) {
            if (a.getTitle().contentEquals(i.getTitle()))
                return true;
        }

        return false;

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

}
