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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class IGNWebScraper implements WebScraper{
    @Autowired
    NewsWebsiteRepository newsWebsites;

    private String url = newsWebsites.findByName("IGN").getRssFeedUrl();
    //private static final String url = "http://feeds.feedburner.com/ign/games-all";
    private List<Article> articles;
    private DateFormat format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");
    //private String siteURL = "https://www.ign.com/";




    public IGNWebScraper() {
        articles = new ArrayList<Article>();
    }

    /**
     * Initiates scrape
     */
    @Override
    public void scrape()
    {
        try {
            //Connects to RSS feed and parses into a document to retrieve article elements
            Document rssFeed = Jsoup.connect(url).get();
            NewsWebsite ign= newsWebsites.findByName("IGN"); //Searches database for IGN

            Elements links = rssFeed.getElementsByTag("item");  //A collection of articles from the parsed URL

            //Searches through each individual article
            for(Element link:links)
            {
                Article curr = createArticle(link,ign);

                    articles.add(curr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an Article object from the extracted url, article title, article
     * publication date, news website, article thumbnail,
     * article description, and calculated impact score
     *
     * @param e HTML element pulled from the RSS feed
     * @param newsSite  Website where the article originated
     * @return  Article
     * @throws ParseException
     */
    @Override
    public Article createArticle(Element e, NewsWebsite newsSite) throws ParseException {

        String title = e.select("title").text();
        String source = e.select("link").text();
        String publicationDate = e.select("pubDate").text();
        Date pubDate = format.parse(publicationDate);

        //TODO
        //Get Last Published Date
        //String lastUpdated=e.select("").text();
        Date lastPubDate = pubDate;

        //Gets a short description of the article for viewing
        String snippet = e.select("description").text();
        if (snippet.length() > 255)
            snippet = snippet.substring(0,255);

        //Placeholder
        int impact = 0;

        String id = UUID.randomUUID().toString();   //Assigns a random ID number for article

        //Placeholder
        Image thumbnail = new Image (id,".jpg",null);

        //TODO
        //Capture article Image
        //
        //Get Impact Score


        return new Article(newsSite.getId(), title, source, newsSite, thumbnail,
                snippet, pubDate, lastPubDate, impact);
    }

    /**
     * Retrieves a list of the extracted news articles by the web scraper
     *
     * @return  List of scraped articles
     */
    @Override
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Retrieves a specific news article provided an index.
     *
     * @param index Index pertaining to an article
     * @return Article
     */
    @Override
    public Article getArticle(int index){
        return articles.get(index);
    }

    /**
     * Checks if newly created article object is already present in list of
     * extracted articles
     * @param a Newly created Article
     * @return Boolean
     */
    @Override
    public Boolean checkDuplicateArticles(Article a){
        for (Article i : articles) {
            if (a.getTitle().contentEquals(i.getTitle()))
                return true;
        }
        return false;
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
