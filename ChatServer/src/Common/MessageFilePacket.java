package Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MessageFilePacket extends ChatPacket{
	public static int packetID = 4;
	
	public static File file;
	private byte[] res;
	
	@Override
	public byte[] generateDataPacket() {
		try {
			FileInputStream fis  = new FileInputStream(file);
			byte[] res = new byte[(int) file.length()];
			
			fis.read(res);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}

}

