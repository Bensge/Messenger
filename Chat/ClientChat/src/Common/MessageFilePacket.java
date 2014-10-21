package Common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MessageFilePacket extends ChatPacket{
	public static int packetID = 4;
	
	public File file;
	public String fileName;
	private byte[] res;
	
	@Override
	public byte[] generateDataPacket() {
		 try {
			System.out.println(file.getPath());
			String fileName = file.getName();
			Path path = Paths.get(file.getPath());
			
			byte[] fileBytes = java.nio.file.Files.readAllBytes(path);
			
			this.length = 4 + /*file name length*/ + fileName.length() + (int) file.length();
			
			res = new byte[length];
			
			MessengerCommon.writeIntToBuffer(fileName.length(), res, 0);
			MessengerCommon.writeBytesToBuffer(fileName.getBytes(), res, 4);
			MessengerCommon.writeBytesToBuffer(fileBytes, res, fileName.length() + 4);
			
			return res;
			
		 }catch (IOException e) {
			System.out.println("damnit");
			e.printStackTrace();
		}
		return null;
		 
		 
	}

}

