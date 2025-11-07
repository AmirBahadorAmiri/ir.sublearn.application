package ir.sublearn.application.models;

public class SongModel {
    private String songId;
    private String songClass;
    private String singerName;
    private String songName;
    private String songUrl;
    private String songPoster;
    private String subLink;
    private String isEnabled;
    private String isPinned;

    public SongModel() {
    }

    public SongModel(String songId, String songClass, String singerName, String songName, String songUrl, String songPoster, String subLink, String isEnabled, String isPinned) {
        this.songId = songId;
        this.songClass = songClass;
        this.singerName = singerName;
        this.songName = songName;
        this.songUrl = songUrl;
        this.songPoster = songPoster;
        this.subLink = subLink;
        this.isEnabled = isEnabled;
        this.isPinned = isPinned;
    }

    public String getSongClass() {
        return songClass;
    }

    public void setSongClass(String songClass) {
        this.songClass = songClass;
    }

    public String getSavedMP3FileName() {
        return getShowSongName() + ".mp3";
    }

    public String getSavedSubFileName() {
        return getShowSongName() + ".lrc";
    }

    public String getShowSongName() {
        return getSingerName() + " - " + getSongName();
    }

    public void setSongPoster(String songPoster) {
        this.songPoster = songPoster;
    }

    public String getSongPoster() {
        return songPoster;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setIsPinned(String isPinned) {
        this.isPinned = isPinned;
    }

    public String getIsPinned() {
        return isPinned;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSubLink(String subLink) {
        this.subLink = subLink;
    }

    public String getSubLink() {
        return subLink;
    }

}
