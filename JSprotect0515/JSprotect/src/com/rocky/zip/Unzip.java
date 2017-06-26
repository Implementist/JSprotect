package com.rocky.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class Unzip {

	// fileNeedUnzip代表要解压的文件, fileUnzipFolder代表要解压的路径
	private String fileNeedUnzip;
	private String fileUnzipFolder;
	public ZipFile zip;
	//private

	public Unzip(String fileNeedUnzip, String fileUnzipFolder)
	{
		this.fileNeedUnzip = fileNeedUnzip;
		this.fileUnzipFolder = fileUnzipFolder;
	}

	public ArrayList<String> unzipFiles(){
		ArrayList<String> unzipEntryNames = new ArrayList<String>();
		try {

			// 缓冲区设置为4096
			/*int bufferSize = 4096;

			System.out.println("unzip" + this.fileNeedUnzip);
			// 创建ZipFile对象, 修饰该Zip文件, 进行解压操作; 并获取该Zip文件中所有的项至names中
			ZipFile zipFile = new ZipFile(this.fileNeedUnzip);
			Enumeration names = zipFile.entries();

			while (names.hasMoreElements())
			{
				ZipEntry entry = (ZipEntry) names.nextElement();

				// 对目录项不进行处理, 只需在目录中文件创建时判断其父路径是否存在, 若不存在, 则创建该父路径即可
				if (!entry.isDirectory())
				{

					InputStream readStream = zipFile.getInputStream(entry);
					System.out.println(entry.getName());

					if (readStream != null)
					{
						// 创建解压后的文件对象
						String fileNameAfterUnzip = this.fileUnzipFolder + File.separator + entry.getName();
						File file = new File(fileNameAfterUnzip);
						unzipEntryNames.add(fileNameAfterUnzip);


						// 若文件所在目录不存在, 则创建该目录
						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdir();
						}

						// 同时利用解压后的文件对象, 构造文件输出流
						FileOutputStream outStream = new FileOutputStream(file);

						// 利用 count来判断文件是否读取到了尾部, 用data开辟一段空间, 来当做
						// 缓冲池, 暂存每次读出的数据, 并写入到输出流
						int count;
						byte[] data = new byte[bufferSize];

						// 从entry中读取数据 and output to the unzip file stream
						while ((count = readStream.read(data, 0, bufferSize)) != -1)
							outStream.write(data, 0, count);

						// 关闭输入和输出流
						outStream.close();
						readStream.close();
					}
				}
			}
			zipFile.close();*/
			File pathFile = new File(fileUnzipFolder);
			if(!pathFile.exists()){
				pathFile.mkdirs();
			}
			zip = new ZipFile(fileNeedUnzip);
			for(Enumeration entries = zip.entries();entries.hasMoreElements();){
				ZipEntry entry = (ZipEntry)entries.nextElement();
				String zipEntryName = entry.getName();
				InputStream in = zip.getInputStream(entry);

				if(zipEntryName.lastIndexOf('/') == zipEntryName.length() -1){
					File file = new File(fileUnzipFolder + File.separator + zipEntryName.substring(0, zipEntryName.length()-1));
					if(!file.exists()){
						file.mkdirs();
					}
					continue;
				}
				//File file = new File(fileUnzipFolder + File.separator + zipEntryName);
				//if(!file.exists())
				//	file.createNewFile();
				unzipEntryNames.add(fileUnzipFolder + File.separator + zipEntryName.replace("/",File.separator));
				System.out.println("Unzip Filename:"+fileUnzipFolder+File.separator+zipEntryName.replace("/",File.separator));
				//System.out.println("it's about time:"+fileUnzipFolder+File.separator+zipEntryName);
				OutputStream out = new FileOutputStream(fileUnzipFolder + File.separator + zipEntryName.replace("/",File.separator));
				byte[] buf1 = new byte[1024];
				int len;
				while((len=in.read(buf1))>0){
					out.write(buf1,0,len);
				}
				in.close();
				out.close();
			}
			for(int i = 0; i < unzipEntryNames.size(); ++i)
				System.out.println(unzipEntryNames.get(i));
			System.out.println("******************解压完毕********************");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return unzipEntryNames;
	}
}
