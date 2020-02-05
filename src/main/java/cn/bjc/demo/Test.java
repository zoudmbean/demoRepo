package cn.bjc.demo;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import util.FastDFSClient;

public class Test {

	public static void main(String[] args) throws Exception {
		String uploadFile = new FastDFSClient("classpath:fdfs_client.conf").uploadFile("D:\\BuyBill.png");
		System.out.println(uploadFile); // group1/M00/00/00/wKgZhV46sJ6AQejEAAAjzmf_q6I252.jpg
	}

	private static void test01() throws FileNotFoundException, IOException, Exception {
		// 1. 加载配置文件  注意：这里的路径需要是绝对路径
		ClientGlobal.init("D:\\pinyougou\\fastDFSdemo\\src\\main\\resources\\fdfs_client.conf");
		// 2. 创建tracker的客户端
		TrackerClient client = new TrackerClient();
		// 3. 通过客户端得到服务端连接对象
		TrackerServer trackerServer = client.getConnection();
		// 4. 声明存储服务端storage
		StorageServer storageServer = null;
		// 5. 获取存储服务器的storage客户端对象
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		
		// 6. 上传文件
		String local_filename = "D:\\BuyBill.png";  // 定义上传的文件路径
		String file_ext_name = "jpg";				// 上传存储的扩展名
		NameValuePair[] meta_list = null;			// 文件扩展信息(也就是在Windows系统中，右键，属性，详细信息中的内容)
		String[] strs = storageClient.upload_file(local_filename, file_ext_name, meta_list);
		// 7. 显示上传的结果（file_id 也就是文件的路径）
		for(String s : strs){
			System.out.println(s); // group1/M00/00/00/wKgZhV46sJ6AQejEAAAjzmf_q6I252.jpg
		}
	}

}
