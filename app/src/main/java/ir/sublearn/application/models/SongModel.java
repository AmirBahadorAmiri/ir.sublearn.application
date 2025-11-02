package ir.sublearn.application.models;

public class SongModel {
    private String songId;
    private String singerId;
    private String singerName;
    private String songName;
    private String songUrl;
    private String songPoster;
    private String singerLogo;
    private String songLike;
    private String subLink;
    private String songSpace;
    private String songLength;
    private String isEnabled;
    private String isPinned;

    public SongModel() {
    }

    public SongModel(String songId, String singerId, String singerName, String songName, String songUrl, String songPoster, String singerLogo, String songLike, String subLink, String songSpace, String songLength, String isEnabled, String isPinned) {
        this.songId = songId;
        this.singerId = singerId;
        this.singerName = singerName;
        this.songName = songName;
        this.songUrl = songUrl;
        this.songPoster = songPoster;
        this.singerLogo = singerLogo;
        this.songLike = songLike;
        this.subLink = subLink;
        this.songSpace = songSpace;
        this.songLength = songLength;
        this.isEnabled = isEnabled;
        this.isPinned = isPinned;
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

    public void setSongLength(String songLength) {
        this.songLength = songLength;
    }

    public String getSongLength() {
        return songLength;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerLogo(String singerLogo) {
        this.singerLogo = singerLogo;
    }

    public String getSingerLogo() {
        return singerLogo;
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

    public void setSongSpace(String songSpace) {
        this.songSpace = songSpace;
    }

    public String getSongSpace() {
        return songSpace;
    }

    public void setIsPinned(String isPinned) {
        this.isPinned = isPinned;
    }

    public String getIsPinned() {
        return isPinned;
    }

    public void setSingerId(String singerId) {
        this.singerId = singerId;
    }

    public String getSingerId() {
        return singerId;
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

    public void setSongLike(String songLike) {
        this.songLike = songLike;
    }

    public String getSongLike() {
        return songLike;
    }
}
