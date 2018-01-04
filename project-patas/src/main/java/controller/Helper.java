package controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;


// Class containing the helper functions
public class Helper {

	// Casts an object to Long
	public static Long objectToLong(Object object) {
		String stringValue = String.valueOf(object);
		Long longValue = Long.parseLong(stringValue);
		return longValue;
	}
	
	// splitCriteriaFromKeys
	// given a list of criteria (strings) in the format "crit":"val",
	//   split them in a hashmap.
	public static HashMap<String, String> splitCriteriaFromKeys(String[] criteria_pairs) {
		HashMap<String, String> criteria_list = new HashMap<String, String>();

		for (int i=1; i < criteria_pairs.length; i++) {
			String pair = criteria_pairs[i];
			String[] splitted_pair = pair.split("\"|:|\"");
			Boolean value_is_not_string = (splitted_pair.length == 4);

			if (value_is_not_string)
				criteria_list.put(splitted_pair[1], splitted_pair[3]);
			else criteria_list.put(splitted_pair[1], splitted_pair[4]);
		}

		return criteria_list;
	}
	
	public static byte[] saveImage(String path){
		BufferedImage img;
		try {
			img = ImageIO.read(new File(path));
			ByteArrayOutputStream bytesImg = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", bytesImg); // TODO: eh soh pra teste TIRAR ISSO
			bytesImg.flush();
			byte[] imgArray = bytesImg.toByteArray();
			bytesImg.close();
			return imgArray;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//ps: chamar o metodo passando dog.getPhoto();
	public static BufferedImage displayImage(byte[] image){
		BufferedImage img = null; 	
		try {
			img = ImageIO.read(new ByteArrayInputStream(image));
			//.setIcon(new ImageIcon(img));	
			 
		    // TODO: eh soh pra teste TIRAR ISSO
			ImageIO.write(img, "PNG", new File("D:/Documentos/patas.png"));
			return img;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
