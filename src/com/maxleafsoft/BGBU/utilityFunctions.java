package com.maxleafsoft.BGBU;

import java.io.*;
import android.util.Log; 

public class utilityFunctions
{
	public static void copyFolders(File sourceLocation, 
								   File targetLocation)
	throws IOException
	{
		if (sourceLocation.isDirectory())
		{
			if (!targetLocation.exists())
			{
				targetLocation.mkdirs();
			}

			String[] children = sourceLocation.list();
			for(int i = 0; i < sourceLocation.listFiles().length; i++)
			{
				copyFolders(new File(sourceLocation, children[i]),
							new File(targetLocation, children[i]));
			}
		}
		else
		{
			if (sourceLocation.toString().toLowerCase().endsWith(".bmp"))
			{
				String fileName = targetLocation.toString().toLowerCase().replace(".bmp",".pmb");
				targetLocation = new File(fileName);
			}
			if (sourceLocation.getName().toString().endsWith(".pmb"))
			{
				String fileName = targetLocation.toString().toLowerCase().replace(".pmb",".bmp");
				targetLocation = new File(fileName);
			}
			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}
}
