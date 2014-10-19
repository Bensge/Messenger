package Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MessageFilePacket extends ChatPacket{
	public static int packetID = 4;
	
	public File file;
	private byte[] res;

	
	@Override
	public byte[] generateDataPacket() {
		try {
			System.out.println("its: " + (file==null));
			res = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			System.out.println("available" + fis.available());
			//dunno whats wrong fuck it
			fis.read(res);
			System.out.println(res==null);
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("falsch");
			e.printStackTrace();
		}
		System.out.println(new String(res));
		return res;
	}

}

