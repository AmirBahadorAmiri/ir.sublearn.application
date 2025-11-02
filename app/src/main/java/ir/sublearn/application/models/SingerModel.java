package ir.sublearn.application.models;

public class SingerModel{
	private String singerId;
	private String singerName;
	private String singerLogo;
	private String isEnabled;

	public SingerModel() {
	}

	public SingerModel(String singerId, String singerName, String singerLogo, String isEnabled) {
		this.singerId = singerId;
		this.singerName = singerName;
		this.singerLogo = singerLogo;
		this.isEnabled = isEnabled;
	}

	public void setSingerId(String singerId){
		this.singerId = singerId;
	}

	public String getSingerId(){
		return singerId;
	}

	public void setIsEnabled(String isEnabled){
		this.isEnabled = isEnabled;
	}

	public String getIsEnabled(){
		return isEnabled;
	}

	public void setSingerName(String singerName){
		this.singerName = singerName;
	}

	public String getSingerName(){
		return singerName;
	}

	public void setSingerLogo(String singerLogo){
		this.singerLogo = singerLogo;
	}

	public String getSingerLogo(){
		return singerLogo;
	}
}
