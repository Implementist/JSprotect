package com.rocky.zip;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

public class Zip {
	private String zipFileName = null;
	private String zipDirectory = null;

	public String getZipFileName() {
		return zipFileName;
	}

	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
		File file = new File(this.zipFileName);

		if(file.exists())
			file.delete();
	}

	public String getZipFileDirectory() {
		return zipDirectory;
	}

	public void setZipFileDirectory(String zipFileDirectory) {
		this.zipDirectory = zipFileDirectory;
	}

	public Zip() {

	}
	public void compress(){
		File zipFile = new File(this.zipFileName);
		File srcDir = new File(this.zipDirectory);
		System.out.println(this.zipDirectory);
		//if(!srcDir.exists())
			//System.out.println("不存在");
		if (!srcDir.exists())
			srcDir.mkdir();
		Project prj = new Project();
		org.apache.tools.ant.taskdefs.Zip zip = new org.apache.tools.ant.taskdefs.Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);

		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcDir);
		zip.addFileset(fileSet);
		zip.execute();
	}

	/*public void compress () throws Exception{

		// 创建要生成的zip文件对象,并创建一个文件输出流
		ZipOutputStream targetZipFile = new ZipOutputStream(new FileOutputStream(this.zipFileName));
		BufferedOutputStream outputStream = new BufferedOutputStream(targetZipFile);

		// 调用zip函数进行递归的文件压缩
		File file = new File(this.zipDirectory);

		//
		if(file.isDirectory()){
			// 获取待压缩目录中所有的文件
			File[] fileLists = file.listFiles();

			// 压缩每一个文件项
			int len = fileLists.length;
			for(int i = 0; i < len; ++i)
				zip(targetZipFile, fileLists[i], fileLists[i].getName(), outputStream);
		}
		else
			zip(targetZipFile, file, file.getName(), outputStream);

		outputStream.close();
		targetZipFile.close();
	}

	public void zip(ZipOutputStream targetZipFile, File file, String base, BufferedOutputStream outputStream) throws IOException {
		//System.out.println("zip");
		if (file.isDirectory())
		{
			//System.out.println(file.getName());
			// 列出待压缩目录中的所有的文件
			File[] fileLists = file.listFiles();

			int len = fileLists.length;

			if (len == 0) {
				targetZipFile.putNextEntry(new ZipEntry(base + "//"));
			}

			for (int i = 0; i < len; ++i) {
				zip(targetZipFile, fileLists[i], base + "//" + fileLists[i].getName(), outputStream);
			}
		}
		else
		{
			targetZipFile.putNextEntry(new ZipEntry(base));

			FileInputStream fInputStream = new FileInputStream(file);

			byte[] buf = new byte[1024];

			int off = 0;
			while ((fInputStream.read(buf, off, 1024)) != -1)
			{
				outputStream.write(buf);
			}

			// 一定要在这里flush掉缓冲区，否则输出会流向下一个文件
			outputStream.flush();
			fInputStream.close();
		}
	}*/
}
