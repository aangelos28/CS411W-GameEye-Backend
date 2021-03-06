package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import edu.odu.cs411yellow.gameeyebackend.common.security.FirebaseSecrets;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.NotificationService;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class NotificationServiceTests {
    @Autowired
    private GameRepository games;

    @Autowired
    private NotificationService notificationService;

    @BeforeAll
    public static void setupFirebase() {
        FirebaseSecrets.read("firebase.json");

        // Initialize firebase
        try {
            InputStream firebaseCredentials = new ByteArrayInputStream(FirebaseSecrets.getCredentials().getBytes(StandardCharsets.UTF_8));
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(firebaseCredentials))
                    .setDatabaseUrl("https://gameeye-8eb07.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void insertTestGames() {
        // Create test games
        Game game1 = new Game();
        game1.setTitle("Fallout 3");
        game1.setIgdbId("1");

        Game game2 = new Game();
        game2.setTitle("Fallout: New Vegas");
        game2.setIgdbId("2");

        Game game3 = new Game();
        game3.setTitle("Watch Dogs: Legion");
        game3.setIgdbId("3");

        // Insert test games
        games.saveAll(Arrays.asList(game1, game2, game3));
    }

    @Test
    public void testSendArticleNotifications() {
        final List<Article> articles = new ArrayList<>();
        final List<String> articleGameIds = new ArrayList<>();

        // Create test articles
        Article firstArticle = new Article();
        firstArticle.setIsImportant(true);
        firstArticle.setTitle("Fallout 3: New Gameplay Trailer");
        firstArticle.setSnippet("New gameplay trailer released for Fallout 3");
        articles.add(firstArticle);
        articleGameIds.add(games.findGameByTitle("Fallout 3").getId());

        Article secondArticle = new Article();
        secondArticle.setIsImportant(false);
        secondArticle.setTitle("Fallout: New Vegas - A Guide to The Mojave Wasteland");
        secondArticle.setSnippet("A guide on how to navigate the Mojave Wasteland");
        articles.add(secondArticle);
        articleGameIds.add(games.findGameByTitle("Fallout: New Vegas").getId());

        Article thirdArticle = new Article();
        thirdArticle.setIsImportant(false);
        thirdArticle.setTitle("Watch Dogs: Legion New Accessories");
        thirdArticle.setSnippet("New phone accessories for Watch Dogs: Legion");
        articles.add(thirdArticle);
        articleGameIds.add(games.findGameByTitle("Watch Dogs: Legion").getId());

        Article fourthArticle = new Article();
        fourthArticle.setIsImportant(true);
        fourthArticle.setTitle("Watch Dogs: Legion Released Today");
        fourthArticle.setSnippet("Watch Dogs: Legion has been released today");
        articles.add(fourthArticle);
        articleGameIds.add(games.findGameByTitle("Watch Dogs: Legion").getId());

        notificationService.sendArticleNotificationsAsync(articles, articleGameIds);
    }

    @AfterEach
    public void deleteTestGames() {
        games.deleteByTitle("Fallout 3");
        games.deleteByTitle("Fallout: New Vegas");
        games.deleteByTitle("Watch Dogs: Legion");

        assertThat(games.findAll().size(), is(0));
    }
}
