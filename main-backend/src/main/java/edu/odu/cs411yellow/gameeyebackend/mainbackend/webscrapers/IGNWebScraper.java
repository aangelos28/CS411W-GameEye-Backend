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

@Service("IgnScrape")
public class IGNWebScraper implements WebScraper{

    private static final String url = "http://feeds.feedburner.com/ign/games-all";
    private List<Article> articles;
    private DateFormat format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");
    //private String siteURL = "https://www.ign.com/";

    @Autowired
    NewsWebsiteRepository siteBuilder;


    public IGNWebScraper(List<Article> articles) {
        this.articles = articles;
    }


    @Override
    public void scrape()
    {
        try {
            //Connects to RSS feed and parses into a document to retrieve article elements
            Document doc = Jsoup.connect(url).get();
            NewsWebsite IGN= siteBuilder.findByName("IGN");
            Elements links = doc.getElementsByTag("item");  //A collection of articles from the parsed URL

            //Searches through each individual article
            for(Element link:links)
            {
                Article curr = createArticle(link,IGN);

                //Adds new Article to list if not already present in list
                if(!checkDuplicateArticles(curr)) {
                    articles.add(curr);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Article createArticle(Element e, NewsWebsite newsSite) throws ParseException {

        String title = e.select("title").text();
        String source = e.select("link").text();
        String publicationDate = e.select("pubDate").text();
        Date pubDate = format.parse(publicationDate);

        //TODO
        //Get Last Published Date
        String lastUpdated=e.select("").text();
        Date lastPubDate = pubDate;

        //Gets a short description of the article for viewing
        String snippet = e.select("description").text();
        if (snippet.length() > 255)
            snippet = snippet.substring(0,255);

        int impact = 0;
        String id = UUID.randomUUID().toString();   //Assigns a random ID number for article

        Image thumbnail = new Image (id,".jpg",null);

        //TODO
        //Capture article Image
        //
        //Get Impact Score


        return new Article(newsSite.getId(), title, source, newsSite, thumbnail,
                snippet, pubDate, lastPubDate, impact);
    }

    @Override
    public List<Article> getArticles() {
        return articles;
    }

    @Override
    public Article getArticle(int index){
        return articles.get(index);
    }


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
