package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.Objects;

/**
 * Represents a news article resource.
 */
public class Article {
    private final String id;

    /**
     * Title of the article.
     */
    private String title;

    /**
     * URL to the page containing the article.
     */
    private String url;

    /**
     * Reference to the news website that this article was retrieved from.
     */
    @DBRef
    private NewsWebsite newsWebsite;

    /**
     * Reference to a thumbnail for this article, if any.
     */
    @DBRef
    private Image thumbnail;

    /**
     * Small portion of the text body of an article, usually the first few sentences.
     * Limit to 255 characters.
     */
    private String snippet;

    /**
     * Date the article was published.
     */
    private Date publicationDate;

    /**
     * Date the document was last updated.
     */
    private Date lastUpdated;

    /**
     * Denotes whether or not an article is important, computed with machine learning.
     */
    private boolean isImportant;

    @PersistenceConstructor
    public Article(String id, String title, String url, NewsWebsite newsWebsite, Image thumbnail, String snippet,
                   Date publicationDate, Date lastUpdated, boolean isImportant) {

        this.id = id;
        this.title = title;
        this.url = url;
        this.newsWebsite = newsWebsite;
        this.thumbnail = thumbnail;
        this.snippet = snippet;
        this.publicationDate = publicationDate;
        this.lastUpdated = lastUpdated;
        this.isImportant = isImportant;
    }

    public Article() {
        this.id = "";
        this.title = "";
        this.url = "";
        this.newsWebsite = new NewsWebsite();
        this.thumbnail = new Image();
        this.snippet = "";
        this.publicationDate = new Date();
        this.lastUpdated = new Date();
        this.isImportant = false;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public NewsWebsite getNewsWebsite() {
        return this.newsWebsite;
    }

    public void setNewsWebsite(NewsWebsite newsWebsite) {
        this.newsWebsite = newsWebsite;
    }

    public Image getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSnippet() {
        return this.snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Date getPublicationDate() {
        return this.publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Date getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean getIsImportant() {
        return this.isImportant;
    }

    public void setIsImportant(boolean isImportant) {
        this.isImportant = isImportant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o ) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article that = (Article) o;

        return Objects.equals(id, that.id)
                && Objects.equals(url, that.url)
                && Objects.equals(newsWebsite, that.newsWebsite)
                && Objects.equals(thumbnail, that.thumbnail)
                && Objects.equals(snippet, that.snippet)
                && Objects.equals(publicationDate, that.publicationDate)
                && Objects.equals(lastUpdated, that.lastUpdated)
                && Objects.equals(isImportant, that.isImportant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, newsWebsite, thumbnail, snippet, isImportant);
    }
}
