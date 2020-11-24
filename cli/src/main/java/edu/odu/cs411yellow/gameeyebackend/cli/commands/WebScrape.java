package edu.odu.cs411yellow.gameeyebackend.cli.commands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Contains CLI commands related to WebScrapeOrchestrator.
 */
@ShellComponent
public class WebScrape {
    WebClient webClient;

    public WebScrape(@Value("${mainbackend.baseurl}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Initiate Scraping of RSS
     */
    @ShellMethod(value = "Initiate Force Scrape of RSSFeeds.", key = "Force-Scrape-All")
    public void forceScrapeAll() {
        String response = this.webClient.post()
                .uri("/private-admin/webscraping/force")
                .retrieve()
                .bodyToMono(String.class)
                .toString();

        System.out.println("Force Scrape of RSS feeds Performed");
    }

    /**
     * Initiate Scraping of RSS
     */
    @ShellMethod(value = "Initiate Force Scrape of MockNews.", key = "Force-Scrape-MockNews")
    public void forceScrapeMockNews() {
        String response = this.webClient.post()
                .uri("/private-admin/webscraping/mockwebsite/force")
                .retrieve()
                .bodyToMono(String.class)
                .toString();

        System.out.println("Force Scrape of Mock News Performed");
    }
}
