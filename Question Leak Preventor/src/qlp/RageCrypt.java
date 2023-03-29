import java.io.*;

class RageCrypt
{
	public static void encrypt(String file,String passphrase)
	{
		File f=new File(file); // in file
		File o=new File(f.getParent()+"/"+f.getName()+".raged"); //out file
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;	
		try
		{
			FileInputStream fis=new FileInputStream(f); // open input stream for in file
			FileOutputStream fos=new FileOutputStream(o); // open output stream for out file
			bytesAvailable = fis.available(); // get the file size in bytes
			bufferSize = Math.min(bytesAvailable,maxBufferSize); // get the minimum buffer size
			buffer = new byte[bufferSize]; // allocate minimum buffer

			bytesRead = fis.read(buffer, 0, bufferSize); // reads in buffer[] from 0 buffersize -1
			int j=0;

			while (bytesRead > 0) { // read until end of file
				for(int i=0;buffer[i]!='\0';i++)
				{
					buffer[i]^=passphrase.charAt(j%passphrase.length()); //encrypts buffer[i] using xor method with passphrase[j%length];
					j++;
				}
				fos.write(buffer, 0, bufferSize);
				bytesAvailable = fis.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fis.read(buffer, 0, bufferSize);
			}		
			fos.flush();
			fos.close();
			fis.close();
		}
		catch (Exception e)
		{}
	}
	public static String decrypt(String file,String passphrase)
	{
		File f=new File(file);
		File o=new File(f.getParent()+"/"+f.getName().replace(".raged",""));
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;	
		try
		{
			FileInputStream fis=new FileInputStream(f);
			FileOutputStream fos=new FileOutputStream(o);
			bytesAvailable = fis.available();
			bufferSize = Math.min(bytesAvailable,maxBufferSize);
			buffer = new byte[bufferSize];

			bytesRead = fis.read(buffer, 0, bufferSize);
			int j=0;

			while (bytesRead > 0) {
				for(int i=0;buffer[i]!='\0';i++)
				{
					buffer[i]^=passphrase.charAt(j%passphrase.length());
					j++;
				}
				fos.write(buffer, 0, bufferSize);
				bytesAvailable = fis.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fis.read(buffer, 0, bufferSize);
			}		
			fos.flush();
			fos.close();
			fis.close();
                        return o.getAbsolutePath();
		}
		catch (Exception e)
		{}
                return "";
	}
}
