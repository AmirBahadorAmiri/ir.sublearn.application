package ir.sublearn.models;

public class EpisodeModel {
    private String episodeId;
    private String seasonId;
    private String movieTitle;
    private String movieFileUrl;
    private String moviePersianSubUrl;
    private String movieEnglishSubUrl;
    private String is_enabled;

    public EpisodeModel() {
    }

    public EpisodeModel(String episodeId, String seasonId, String movieTitle, String movieFileUrl, String moviePersianSubUrl, String movieEnglishSubUrl, String is_enabled) {
        this.episodeId = episodeId;
        this.seasonId = seasonId;
        this.movieTitle = movieTitle;
        this.movieFileUrl = movieFileUrl;
        this.moviePersianSubUrl = moviePersianSubUrl;
        this.movieEnglishSubUrl = movieEnglishSubUrl;
        this.is_enabled = is_enabled;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getIs_enabled() {
        return is_enabled;
    }

    public void setIs_enabled(String is_enabled) {
        this.is_enabled = is_enabled;
    }

    public void setMovieFileUrl(String movieFileUrl) {
        this.movieFileUrl = movieFileUrl;
    }

    public String getMovieFileUrl() {
        return movieFileUrl;
    }

    public void setMoviePersianSubUrl(String moviePersianSubUrl) {
        this.moviePersianSubUrl = moviePersianSubUrl;
    }

    public String getMoviePersianSubUrl() {
        return moviePersianSubUrl;
    }

    public void setMovieEnglishSubUrl(String movieEnglishSubUrl) {
        this.movieEnglishSubUrl = movieEnglishSubUrl;
    }

    public String getMovieEnglishSubUrl() {
        return movieEnglishSubUrl;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

}
