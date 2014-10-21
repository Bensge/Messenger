package Common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MessageImagePacket extends MessageFilePacket {
	public static int packetID = 5;

	public BufferedImage getBufferedImage() {
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//public BufferedImage image;
	
	
	public byte[] generateDataPacket()
	{
		return super.generateDataPacket();
		/*// convert BufferedImage to byte array
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] imageData = null;
		try {
			ImageIO.write(image, "png", outputStream);
			outputStream.flush();
			imageData = outputStream.toByteArray();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.length = imageData.length;
		return imageData;*/
	}
}
