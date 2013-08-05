package com.fruit.util.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.fruit.util.CommUtil;
import com.fruit.util.MD5;
import com.fruit.util.net.GzipDecompressingEntity;
import com.fruit.util.net.NetUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FileUtil {
	
	public static String localHtmlPrefix = "file://";
	
	public static String localHtmlIndexName = "index.html";
	
	public static String sdCardPathStr = Environment.getExternalStorageDirectory() + "/" + CommUtil.TAG + "/";
	
	public static String sdCardPathImgStr = Environment.getExternalStorageDirectory() + "/" + CommUtil.TAG + "/img/";
	
	public static String sdCardPathZipStr = Environment.getExternalStorageDirectory() + "/" + CommUtil.TAG + "/zip/";
	
	public static String sdCardPathApkStr = Environment.getExternalStorageDirectory() + "/" + CommUtil.TAG + "/apk/";
	
	public static String sdCardPathZipTmpStr = Environment.getExternalStorageDirectory() + "/" + CommUtil.TAG + "/ziptmp/";
	
	public static String sdCardPathWebViewCacheStr = "";
	
	public static boolean sdCardPathFlag = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	
	public static int WRITE_REPLACE = 0;//写文件时，无论文件存在与否都写成最新的文件
	
	public static int WRITE_NOREPLACE = 1;//写文件时，如果文件存在则不写
	
	public static long MAXFILESIZE = 1099511627776l;//设的最大的下载文件的大小（1G）

	/**
	 * 根据文件名生成由md5生成的新的文件名
	 * 
	 * @param str
	 * @return
	 */
	public static String fileNameMd5(String str) {
		
		str = CommUtil.getStrVal(str);
		
		if (str.lastIndexOf(".") > -1) {
			str = MD5.getMd5(str.substring(0, str.lastIndexOf(".")).getBytes())
					+ str.substring(str.lastIndexOf("."), str.length());
		}
		
		return str;
	}

	public static void createDir(String fileDirStr) {
		File tempfile = new File(fileDirStr);
		if (!tempfile.isDirectory()) {
			tempfile.mkdirs();
		}
	}

	public static void createFile(String filePathStr) {
		try {
			File file = new File(filePathStr);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAllFiles(File file){
        if (file.exists() && file.canRead()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteAllFiles(files[i]);
                }
            }
            file.delete();//删除空文件夹，此步相当关键！
        }
    }
	
	//删除子文件及文件夹
	public static void deleteAllSonFiles(File file) {
		if (file.isDirectory()) {
			File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteAllFiles(files[i]);
            }
		}
	}
	
	/**
	 * 根据流读取字节数组
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream in) {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[2048];
			int len = -1;
			while ((len = in.read(buffer)) != -1) {
				bao.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bao.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bao.toByteArray();
	}
	
	/**
	 * 根据文件名读取字节数组
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(String filePath) {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				bao.write(buffer, 0, len);
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bao.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bao.toByteArray();
	}
	
	/**
	 * 根据文件名读取字节数组
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(String filePath, Context context) {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			FileInputStream fis = context.openFileInput(filePath);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				bao.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bao.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bao.toByteArray();
	}

	/**
	 * 下载网络文件并写入SD卡中
	 * @param urlStr
	 * @param filePath
	 * @return
	 */
	public static boolean downloadFile2SD(String urlStr, String sdCardFilePath, int writeType) {
		
//		urlStr = urlStr.replace("http://test2.hiediting.com", "http://172.17.19.138");
		
		boolean flag = false;
		
		boolean writeFlag = true;
		
		if (writeType == WRITE_NOREPLACE) {
			File tmpFile = new File(sdCardFilePath);
			writeFlag = !tmpFile.exists();
			flag = true;
		}
		if (writeFlag) {
			HttpClient httpClient = NetUtil.getHttpClient();
			InputStream in = null;
			try {
				HttpGet httpGet = new HttpGet(urlStr);
				httpGet.setHeader("Accept-Encoding", NetUtil.AcceptEncoding);
				httpGet.setHeader("Accept", NetUtil.Accept);
				HttpResponse response = httpClient.execute(httpGet);
				HttpEntity httpEntity = response.getEntity();
				Header header = httpEntity.getContentEncoding();
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					
					if (header != null && header.getValue().equalsIgnoreCase("gzip")) {
						GzipDecompressingEntity gzipEntity = new GzipDecompressingEntity(
								httpEntity);
						in = gzipEntity.getContent();
					} else {
						in = httpEntity.getContent();
					}
				}
				if (sdCardPathFlag && in != null) {
//					String sdCardFilePath = sdCardPathStr + filePath;
					createDir(sdCardFilePath.substring(0, sdCardFilePath.lastIndexOf("/")));
					createFile(sdCardFilePath);
					FileOutputStream fos = null;
					fos = new FileOutputStream(sdCardFilePath);
					byte[] buffer = new byte[2048];
					int len = -1;
					while ((len = in.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					flag = true;
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			} finally {
				httpClient.getConnectionManager().shutdown();
				if (in != null) {
					try {
						in.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					in = null;
				}
			}
		}
		
		return flag;
	}
	
	/**
	 * 写入Memory中
	 * @param urlStr
	 * @param filePath
	 * @return
	 */
	public static boolean copy2Memory(String urlStr, String filePath, Context context) {

		boolean flag = true;
		try {
			InputStream in = null;
			FileOutputStream fos = context.openFileOutput(filePath, Context.MODE_PRIVATE);   
			fos.write(readStream(in));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	public static String downloadImgForStr(String imgUrl) {
		String imgPath = "";
		if (imgUrl.indexOf(".") > -1) {
			String imgNameMd5 = FileUtil.fileNameMd5(imgUrl);
			FileUtil.downloadFile2SD(imgUrl, FileUtil.sdCardPathImgStr + imgNameMd5, FileUtil.WRITE_NOREPLACE);
			imgPath = FileUtil.sdCardPathImgStr + imgNameMd5;
		}
		return imgPath;
	}
	
	public static byte[] downloadImgForByte(String imgUrl) {
		byte[] imgByte = null; 
		if (imgUrl.indexOf(".") > -1) {
			String imgNameMd5 = FileUtil.fileNameMd5(imgUrl);
			FileUtil.downloadFile2SD(imgUrl, FileUtil.sdCardPathImgStr + imgNameMd5, FileUtil.WRITE_NOREPLACE);
			imgByte = FileUtil.readStream(FileUtil.sdCardPathImgStr + imgNameMd5);
		}
		return imgByte;
	}
	
	public static Bitmap downloadImgForBitmap(String imgUrl) {
		Bitmap bitmap = null;
		String imgUrlStr = imgUrl;
		String imgNameMd5 = FileUtil.fileNameMd5(imgUrlStr);
		FileUtil.downloadFile2SD(imgUrlStr, FileUtil.sdCardPathImgStr + imgNameMd5, FileUtil.WRITE_NOREPLACE);
		if (new File(FileUtil.sdCardPathImgStr + imgNameMd5).exists()) {
			bitmap = BitmapFactory.decodeFile(FileUtil.sdCardPathImgStr + imgNameMd5);
		}
		return bitmap;
	}
	
	
	/**
	 * 下载zip包并解压
	 * @param zipUrl
	 */
	public static boolean downloadZip(String zipUrl, String zipPath, String zipFolder, int writeType) {
		boolean flag = false;
		
		if (downloadFile2SD(zipUrl, zipFolder + zipPath, writeType)) {
			File zipFile = new File(zipFolder + zipPath);
			if (zipFile.exists()) {
				String foldStr = zipPath.substring(0, zipPath.lastIndexOf("."));
				flag = unZip(zipFolder + zipPath, zipFolder + foldStr + "/");
				zipFile.delete();
			}
		}
		return flag;
	}
	
	public static boolean unZip(String unZipfileName, String unZipMenu) {// unZipfileName需要解压的zip文件名
		boolean unzipFlag = true;
		ZipInputStream zipIn = null;
		ZipEntry zipEntry = null;
		FileOutputStream fileOut = null;
		int readedBytes;
		File file;
		try {
			zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(unZipfileName)));
			while ((zipEntry = zipIn.getNextEntry()) != null) {
				file = new File(unZipMenu + "/" + zipEntry.getName());
				if (zipEntry.isDirectory()) {
					file.mkdirs();
				} else {
					// 如果指定文件的目录不存在,则创建之.
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					fileOut = new FileOutputStream(file);
					byte[] buf = new byte[2048];
					while ((readedBytes = zipIn.read(buf)) > 0) {
						fileOut.write(buf, 0, readedBytes);
					}
					fileOut.close();
				}
				zipIn.closeEntry();
			}
			zipIn.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			unzipFlag = false;
		} finally {
			try {
				if (fileOut != null) {
					fileOut.close();
					fileOut = null;
				}
				if (zipIn != null) {
					zipIn.close();
					zipIn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return unzipFlag;
	}
	
	/**
     * 生产文件 如果文件所在路径不存在则生成路径
     * 
     * @param fileName
     *            文件名 带路径
     * @param isDirectory 是否为路径
     * @return
     */
    public static File buildFile(String fileName, boolean isDirectory) {
        File target = new File(fileName);
        if (isDirectory) {
            target.mkdirs();
        } else {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
                target = new File(target.getAbsolutePath());
            }
        }
        return target;
    }
}
