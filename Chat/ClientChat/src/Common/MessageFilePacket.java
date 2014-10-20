package Common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MessageFilePacket extends ChatPacket{
	public static int packetID = 4;
	
	public File file;
	private byte[] res;

	
	@Override
	public byte[] generateDataPacket() {
		 try {
			System.out.println(file.getPath());
			Path path = Paths.get(file.getPath());
			res = java.nio.file.Files.readAllBytes(path);
			this.length = res.length;
			return res;
			
		 }catch (IOException e) {
			System.out.println("damnit");
			e.printStackTrace();
		}
		return null;
		 
		 
	}

}

