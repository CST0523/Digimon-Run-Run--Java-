package model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class DigimonPicker extends VBox {

	private ImageView circleImage;
	private ImageView digimonImage;
	
	private String circleNotChoosen = "/resources/grey_circle.png";
	private String circleChoosen = "/resources/red_choosen.png";
	
	private DIGIMON DIGIMON;
	
	private boolean isCircleChoosen;
	
	
	public DigimonPicker(DIGIMON DIGIMON) {
		circleImage = new ImageView(circleNotChoosen);
		digimonImage = new ImageView(DIGIMON.getUrl());
		this.DIGIMON = DIGIMON;
		isCircleChoosen = false;
		this.setAlignment(Pos.CENTER);
		this.setSpacing(20);
		this.getChildren().add(circleImage);
		this.getChildren().add(digimonImage);
		
	}
	
	public DIGIMON getDigimon() {
		return DIGIMON;
	}
	
	public boolean getCircleChoosen() {
		return isCircleChoosen;
	}
	
	public void setIsCircleChoosen(boolean isCircleChoosen) {
		this.isCircleChoosen = isCircleChoosen;
		String imageToSet = this.isCircleChoosen ? circleChoosen : circleNotChoosen;
		circleImage.setImage(new Image(imageToSet));
	}
}
