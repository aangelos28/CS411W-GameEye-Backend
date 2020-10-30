package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.ResourceNotifications;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.responses.WatchedGameResponse;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.responses.WatchedGameShortResponse;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing user watchlists.
 */
@Service
public class WatchlistService {

    UserRepository users;
    GameRepository games;

    Logger logger = LoggerFactory.getLogger(WatchlistService.class);

    @Autowired
    public WatchlistService(UserRepository users, GameRepository games) {
        this.users = users;
        this.games = games;
    }

    /**
     * Get the watchlist of a user.
     *
     * @param firebaseId The firebase id of the user.
     * @return List of watched games.
     */
    public List<WatchedGameResponse> getWatchlistGames(final String firebaseId) {
        final User user = users.findUserByFirebaseId(firebaseId);

        List<WatchedGame> watchedGames = user.getWatchList();
        List<WatchedGameResponse> watchedGameResponses = new ArrayList<>(watchedGames.size());

        for (WatchedGame watchedGame : watchedGames) {
            String gameId = watchedGame.getGameId();
            Game game = games.findGameById(gameId);

            WatchedGameResponse watchedGameResponse = new WatchedGameResponse(watchedGame);
            watchedGameResponse.setTitle(game.getTitle());
            watchedGameResponse.setLogoUrl(game.getLogoUrl());
            watchedGameResponses.add(watchedGameResponse);
        }

        return watchedGameResponses;
    }

    /**
     * Get the watchlist of a user. Returns a short result.
     *
     * @param firebaseId The firebase id of the user.
     * @return List of watched games.
     */
    public List<WatchedGameShortResponse> getWatchlistGamesShort(final String firebaseId) {
        final User user = users.findUserByFirebaseId(firebaseId);

        List<WatchedGame> watchedGames = user.getWatchList();
        List<WatchedGameShortResponse> watchedGameResponses = new ArrayList<>(watchedGames.size());

        for (WatchedGame watchedGame : watchedGames) {
            String gameId = watchedGame.getGameId();
            Game game = games.findGameById(gameId);

            WatchedGameShortResponse watchedGameResponse = new WatchedGameShortResponse();
            watchedGameResponse.setId(gameId);
            watchedGameResponse.setTitle(game.getTitle());
            watchedGameResponses.add(watchedGameResponse);
        }

        return watchedGameResponses;
    }

    /**
     * Gets the watchlist entry with index i.
     *
     * @param firebaseId The firebase id of the user.
     * @param index      Watchlist entry index
     * @return Watched game undex index i
     */
    public WatchedGameResponse getWatchlistGame(final String firebaseId, final int index) {
        final User user = users.findUserByFirebaseId(firebaseId);

        final WatchedGame watchedGame = user.getWatchList().get(index);
        final Game game = games.findGameById(watchedGame.getGameId());

        final WatchedGameResponse watchedGameResponse = new WatchedGameResponse(watchedGame);
        watchedGameResponse.setTitle(game.getTitle());
        watchedGameResponse.setLogoUrl(game.getLogoUrl());

        return watchedGameResponse;
    }

    /**
     * Gets the watchlist entry with index i.
     *
     * @param firebaseId The firebase id of the user.
     * @param index      Watchlist entry index
     * @return Watched game undex index i
     */
    public WatchedGameShortResponse getWatchlistGameShort(final String firebaseId, final int index) {
        final User user = users.findUserByFirebaseId(firebaseId);

        final WatchedGame watchedGame = user.getWatchList().get(index);
        final String gameId = watchedGame.getGameId();
        final Game game = games.findGameById(gameId);

        final WatchedGameShortResponse watchedGameResponse = new WatchedGameShortResponse();
        watchedGameResponse.setId(gameId);
        watchedGameResponse.setTitle(game.getTitle());

        return watchedGameResponse;
    }

    /**
     * Add a game to a user's watchlist.
     *
     * @param firebaseId The firebase id of the user.
     * @param gameId     Id of the game to add.
     */
    public void addWatchlistGame(final String firebaseId, final String gameId) throws Exception {
        final User user = this.users.findUserByFirebaseId(firebaseId);
        final Game game = this.games.findGameById(gameId);

        if (game == null) {
            throw new Exception("Game not found, cannot add to watchlist.");
        }

        final List<WatchedGame> watchlist = user.getWatchList();
        final WatchedGame newGame = new WatchedGame(game.getId(), 0, new ResourceNotifications());

        // Ensure we do not add a duplicate game
        if (!watchlist.contains(newGame)) {
            // Add game to watchlist
            watchlist.add(newGame);
            users.save(user);

            // Increment the number of watchers for the game
            games.incrementWatchers(gameId);
        }
    }

    /**
     * Delete a game from a user's watchlist.
     *
     * @param firebaseUserId The firebase id of the user.
     * @param gameIndex      Index of the game in the watchlist to delete.
     */
    public void deleteWatchlistGameByIndex(final String firebaseUserId, final int gameIndex) throws Exception {
        final User user = this.users.findUserByFirebaseId(firebaseUserId);

        // Disallow negative game indices
        if (gameIndex < 0) {
            throw new Exception("Negative game indices not allowed.");
        }

        final List<WatchedGame> watchlist = user.getWatchList();

        // Disallow game indices out of watchlist bounds
        if (gameIndex >= watchlist.size()) {
            throw new Exception(String.format("Game index %d out of bounds for watchlist of size %d", gameIndex, watchlist.size()));
        }

        // Remove game from watchlist
        final String gameId = watchlist.get(gameIndex).getGameId();
        watchlist.remove(gameIndex);
        users.save(user);

        // Increment the number of watchers for the game
        games.decrementWatchers(gameId);
    }

    /**
     * Delete a game from a user's watchlist.
     *
     * @param firebaseUserId The firebase id of the user.
     * @param gameId         Id of the game to remove.
     */
    public void deleteWatchlistGameById(final String firebaseUserId, final String gameId) throws Exception {
        final User user = this.users.findUserByFirebaseId(firebaseUserId);

        final List<WatchedGame> watchlist = user.getWatchList();
        boolean removed = watchlist.removeIf(watchedGame -> watchedGame.getGameId().equals(gameId));

        // Remove game from watchlist
        if (removed) {
            users.save(user);
        } else {
            throw new Exception("Could not remove game from watchlist as it was not found.");
        }

        // Increment the number of watchers for the game
        games.decrementWatchers(gameId);
    }
}
