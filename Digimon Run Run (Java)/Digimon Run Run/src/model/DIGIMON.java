package model;

public enum DIGIMON {
	
	AGUMON("/resources/digimon1.gif", "/resources/digimon1_life.png"),
	TOGEMON("/resources/digimon2.gif", "/resources/digimon2_life.png"),
	GABUMON("/resources/digimon3.gif", "/resources/digimon3_life.png"),
	CALUMON("/resources/digimon4.gif", "/resources/digimon4_life.png");
	
	String urlCharacter;
	String urlLife;
	
	private DIGIMON(String urlCharacter, String urlLife) {
		this.urlCharacter = urlCharacter;
		this.urlLife = urlLife;
	}
	
	public String getUrl() {
		return this.urlCharacter;
	}
	
	public String getUrlLife() {
		return urlLife;
	}

}
