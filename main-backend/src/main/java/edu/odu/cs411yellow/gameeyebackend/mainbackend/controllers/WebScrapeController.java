package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers.MockNewsScraper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers.WebScraperOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebScrapeController {

    WebScraperOrchestrator webScraperOrchestrator;

    @Autowired
    MockNewsScraper mock;

    @Autowired
    public WebScrapeController (WebScraperOrchestrator webScraperOrchestrator){
        this.webScraperOrchestrator = webScraperOrchestrator;
    }

    /**
     * Perform ForceScrape on RSS feeds
     *
     */
    @PostMapping(path = "/private-admin/webscraping/force")
    public ResponseEntity<?> performForceScrapeRSS() {
        webScraperOrchestrator.forceScrape();
        return ResponseEntity.ok("Force Scrape of RSS feeds Performed");
    }

    /**
     * Perform ForceScrape on mocknewsSite
     *
     */
    @PostMapping(path = "/private-admin/webscraping/mockwebsite/force")
    public ResponseEntity<?> performForceScrapeMockSite() {
        webScraperOrchestrator.forceScrape(mock);
        return ResponseEntity.ok("Force Scrape of Mock News Performed");
    }

}
