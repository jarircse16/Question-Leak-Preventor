import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

class QLP
{
	private static String folder,passphrase;

	public static void setFolder(String f)
	{
		folder=f;
	}
	public static void setPass(String p)
	{
		passphrase=p;
	}
	
	private static boolean download(String arg,String name)
	{
		try {
			URL url = new URL(arg); // Build URL from string - arg
			String path = "./"+name; // set the download path

			URLConnection connection = url.openConnection(); // open url connection
			connection.connect(); // connect 

			InputStream input = new BufferedInputStream(url.openStream()); // get input string for reading data
			OutputStream output = new FileOutputStream(path); // open file output stream to write

			byte data[] = new byte[1024];
			int count;
			while ((count = input.read(data)) != -1) { // read until the end of input stream
				output.write(data, 0, count); // write from data[0] to data[count] to file
			}

			output.flush(); // save changes
			output.close(); // close streams
			input.close();
			return true;
		} catch (Exception e) { // error handing
                    
		}
		return false;
	}
	public static void Wait()
	{
            Random r=new Random(); // init random class
            int n; 
            
            // 
            String selected_q;
            String selected_p;
            
		for(;;)
		{ // loop until we get the question
                        n=r.nextInt(Constants.x); // get a random number from 0 to x-1
                        selected_q=String.valueOf(n)+".jpg.raged"; // nth random question
                        selected_p=String.valueOf(n)+".jpg.pass"; // nth password
                        
                        System.out.println("Trying for "+selected_q+"...");
                        
			Boolean file=download(Constants.server+"Question/"+selected_q,selected_q);
			Boolean pass=download(Constants.server+"Question/"+selected_p,selected_p);
			if(file==true&&pass==true)
			{
				break;
			}
			try
			{
				Thread.sleep(5000); // try - catch
			}
			catch (InterruptedException e) // error handling
			{}
			
		}
		File p=new File("./"+selected_p);
		File q=new File("./"+selected_q);
		try
		{
			BufferedReader br =new BufferedReader(new FileReader(p.getAbsolutePath())); // open file for reading
			try{
				StringBuilder sb =new StringBuilder(); // ""
				String line = br.readLine(); // read a line from file
				sb.append(line);
				String passphrase= sb.toString(); // convert to string
				br.close(); // close stream
				String er=RageCrypt.decrypt(q.getAbsolutePath(),passphrase); // decrypt question with passphrase
                                Process pro; 
                                pro = Runtime.getRuntime().exec("./print.exe \""+er+"\" /print"); // executes system command to print the question
                                pro.waitFor(); // wait until the process exits
			}
			catch(Exception e)
			{}
		}
		catch (Exception e)
		{}
	}
	public static void work()
	{
		//Encryption using ragecrypt
		System.out.println("Encrypting files with passphrase....");
                System.out.println("Creating passphrase files....\n");
                File fol=new File(folder); // get the folder
                File[] files=fol.listFiles(); // store all the question in folder in files[]
               /*
                int y=files.length;
                Random r=new Random();
                int n=r.nextInt(y);
                File FILE=files[n];
                */
                String file="";
                for(int i=0;i<files.length;i++)
                {
                    file=files[i].getAbsolutePath(); //  set the full path of questions in file variable
                    RageCrypt.encrypt(file,passphrase); // encrypt the question with passphrase
                    File f=new File(file);
                    File pass=new File(f.getParent()+"/"+f.getName()+".pass");
                    try
                    {
                            pass.createNewFile(); // create the pass file 
                            FileOutputStream fos=new FileOutputStream(pass); // open pass file stream to write
                            fos.write(passphrase.getBytes()); // convert passphrase to bytes and write it in pass file
                            fos.flush(); // save changes
                            fos.close(); // close
                    }
                    catch (Exception e)
                    {

                    }
                    System.out.println(upload(file+".raged",i,".jpg.raged"));
                    System.out.println(upload(file+".pass",i,".jpg.pass")); // upload both encrypted question and password to server
                }
		System.out.println("\nDone.");
            try {
                Runtime.getRuntime().exec("del "+folder+"/*.raged /f /q");
                Runtime.getRuntime().exec("del "+folder+"/*.pass /f /q");
            } catch (IOException ex) {
                
            }
	}
	public static String upload(String arg,int n,String p)
	{
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		FileInputStream fileInputStream = null;

		String urlServer = Constants.server+"upload.php?d=Question";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****"; // data splitter boundary

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		File f=new File(arg);
                System.out.println(f.getName()); // print currently uploading file name
                
		try {
			fileInputStream = new FileInputStream(f); // open for reading file
			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();
                            // connect to server for uploading
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true); // for uploading data
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");
                                
                        // keep the connection alive 
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
										  "multipart/form-data;boundary=" + boundary);
                                                                                  // set boundary and form data type
			outputStream = new DataOutputStream(connection.getOutputStream()); // for writing the file in server
			outputStream.writeBytes(twoHyphens + boundary + lineEnd); // write the boundary
			outputStream.writeBytes("Content-Disposition: form-data;name=\"file\";filename=\"" //name = file cause server backend php will receive the file naming $_FILES['file']
									+ String.valueOf(n)+p + "\"" + lineEnd); // file name ( number actually ) + the extension
			outputStream.writeBytes(lineEnd);

                        
			bytesAvailable = fileInputStream.available(); // get total file size in bytes
			bufferSize = Math.min(bytesAvailable, maxBufferSize); // set the minimum buffer size so that we don't cross memory limit
			buffer = new byte[bufferSize]; // allocate memory for buffer

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize); // read into buffer[] from 0 to bufferSize -1

			while (bytesRead > 0) { // read until bytesRead=0
				outputStream.write(buffer, 0, bufferSize); // write buffer[] in output stream file
				bytesAvailable = fileInputStream.available(); // again get total file size in bytes
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);  // try reading the file again
			}

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
									+ lineEnd); // send that file is fully uploaded

			// Responses from the server (code and message)
			String serverResponseCode = String.valueOf(connection.getResponseCode()); // convert response code to str
			String serverResponseMessage = connection.getResponseMessage(); // OK

			//return serverResponseMessage;
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();

			return "Response Code: "+serverResponseCode+"\r\nDetails: "+serverResponseMessage;
		} catch (Exception ex) {

		}
		return "";
	}
}

