package ir.sublearn.application.models;

import ir.sublearn.application.tools.iokhttp.IOkHttp;

public class SeriesModel {
    private String seriesId;
    private String seriesName;
    private String seriesPoster;
    private String is_enabled;

    public SeriesModel() {
    }

    public SeriesModel(String seriesId, String seriesName, String seriesPoster, String is_enabled) {
        this.seriesId = seriesId;
        this.seriesName = seriesName;
        this.seriesPoster = seriesPoster;
        this.is_enabled = is_enabled;
    }

    public String getIs_enabled() {
        return is_enabled;
    }

    public void setIs_enabled(String is_enabled) {
        this.is_enabled = is_enabled;
    }

    public void setSeriesPoster(String seriesPoster) {
        this.seriesPoster = seriesPoster;
    }

    public String getSeriesPoster() {
        return seriesPoster;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesId() {
        return seriesId;
    }
}
