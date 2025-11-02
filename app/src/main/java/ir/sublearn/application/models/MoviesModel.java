package ir.sublearn.application.models;

public class MoviesModel{
	private String movieId;
	private String movieName;
	private String moviePoster;
	private String movieFileUrl;
	private String moviePersianSubUrl;
	private String movieEnglishSubUrl;
	private String isEnabled;

	public MoviesModel() {
	}

	public MoviesModel(String movieId, String movieName, String moviePoster, String movieFileUrl, String moviePersianSubUrl, String movieEnglishSubUrl, String isEnabled) {
		this.movieId = movieId;
		this.movieName = movieName;
		this.moviePoster = moviePoster;
		this.movieFileUrl = movieFileUrl;
		this.moviePersianSubUrl = moviePersianSubUrl;
		this.movieEnglishSubUrl = movieEnglishSubUrl;
		this.isEnabled = isEnabled;
	}

	public void setIsEnabled(String isEnabled){
		this.isEnabled = isEnabled;
	}

	public String getIsEnabled(){
		return isEnabled;
	}

	public void setMovieFileUrl(String movieFileUrl){
		this.movieFileUrl = movieFileUrl;
	}

	public String getMovieFileUrl(){
		return movieFileUrl;
	}

	public void setMoviePersianSubUrl(String moviePersianSubUrl){
		this.moviePersianSubUrl = moviePersianSubUrl;
	}

	public String getMoviePersianSubUrl(){
		return moviePersianSubUrl;
	}

	public void setMovieEnglishSubUrl(String movieEnglishSubUrl){
		this.movieEnglishSubUrl = movieEnglishSubUrl;
	}

	public String getMovieEnglishSubUrl(){
		return movieEnglishSubUrl;
	}

	public void setMovieName(String movieName){
		this.movieName = movieName;
	}

	public String getMovieName(){
		return movieName;
	}

	public void setMovieId(String movieId){
		this.movieId = movieId;
	}

	public String getMovieId(){
		return movieId;
	}

	public void setMoviePoster(String moviePoster){
		this.moviePoster = moviePoster;
	}

	public String getMoviePoster(){
		return moviePoster;
	}
}
