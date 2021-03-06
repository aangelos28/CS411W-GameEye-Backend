package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticGameRepositoryCustom {
    SearchHits<ElasticGame> autocompleteGameTitle(final String title, final int maxResults);
    boolean existsByTitle(final String title);
}
