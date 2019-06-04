package com.yusun.cartracker.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import android.content.Context;

public class FileUtils {	
	public static String fileToMD5(String filePath) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath);
			byte[] buffer = new byte[1024];
			MessageDigest digest = MessageDigest.getInstance("MD5");
			int numRead = 0;
			while (numRead != -1) {
				numRead = inputStream.read(buffer);
				if (numRead > 0)
					digest.update(buffer, 0, numRead);
			}
			byte[] md5Bytes = digest.digest();
			return new String(encodeHex(md5Bytes));
		} catch (Exception e) {
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private static char[] encodeHex(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}
		return out;
	}

	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路�? 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路�? 如：f:/fqf.txt
	 * @return boolean
	 */
	static public boolean copyFile(String oldPath, String newPath) {
		boolean isok = true;
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在�?
				InputStream inStream = new FileInputStream(oldPath); // 读入原文�?
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					// System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
				fs.close();
				inStream.close();
			} else {
				isok = false;
			}
		} catch (Exception e) {
			// System.out.println("复制单个文件操作出错");
			// e.printStackTrace();
			isok = false;
		}
		return isok;

	}

	/**
	 * 复制整个文件夹内�?
	 * 
	 * @param oldPath
	 *            String 原文件路�? 如：c:/fqf
	 * @param newPath
	 *            String 复制后路�? 如：f:/fqf/ff
	 * @return boolean
	 */
	static public boolean copyFolder(String oldPath, String newPath) {
		boolean isok = true;
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件�?
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 100];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
						Thread.yield();
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件�?
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			isok = false;
		}
		return isok;
	}

}
