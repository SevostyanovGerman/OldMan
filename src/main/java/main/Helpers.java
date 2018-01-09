package main;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Helpers {

	static public String getUrl(String referer) {
		String url;
		int index = referer.indexOf("?");
		if (index > 0) {
			url = referer.substring(0, index);
		} else {
			url = referer;
		}
		return url;
	}

	static public byte[] convertToByteArray(BufferedImage bufferedImage) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", outputStream);
		outputStream.flush();
		byte[] bytes = outputStream.toByteArray();
		outputStream.close();
		return bytes;
	}

	static public BufferedImage resizePicture(BufferedImage originalImage, int type, int IMG_WIDTH) {
		int widthImage = originalImage.getWidth();
		int highImage = originalImage.getHeight();
		int resizedHigh = (highImage * IMG_WIDTH) / widthImage;
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, resizedHigh, type);
		Graphics2D graphics = resizedImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics.drawImage(originalImage, 0, 0, IMG_WIDTH, resizedHigh, null);
		return resizedImage;
	}
}
