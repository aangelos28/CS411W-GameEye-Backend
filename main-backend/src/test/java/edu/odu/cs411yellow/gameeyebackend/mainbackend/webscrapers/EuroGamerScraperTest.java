package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class EuroGamerScraperTest {

    public EuroGamerScraper egTest;

    //TODO Access the test controller

    @BeforeEach
    public void setUp() {
        egTest = new EuroGamerScraper();
    }

    //TODO write Unit Tests
    //Take a SnapShot of the Rss feed
    @Test
    public void testScrape() {

        System.out.print(egTest.toString());
        assert (true);
    }


}
