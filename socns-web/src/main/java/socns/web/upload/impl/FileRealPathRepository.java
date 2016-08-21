/**
 * 
 */
package socns.web.upload.impl;

import java.io.File;
import java.io.IOException;

import mtons.modules.utils.GMagickUtils;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import socns.core.context.AppContext;
import socns.core.utils.FileNameUtils;
import socns.web.upload.Repository;
import socns.web.utils.ImageUtil;

/**
 * @author langhsu
 *
 */
public class FileRealPathRepository extends AbstractFileRepository implements Repository {
	@Autowired
	private AppContext appContext;
	
	@Override
	public String temp(MultipartFile file, String basePath) throws IOException {
		validateFile(file);
		
		String root = appContext.getRoot();
		
		String name = FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));
		String path = basePath + "/" + name;
		File temp = new File(root + path);
		checkDirAndCreate(temp);
		file.transferTo(temp);
		return path;
	}
	
	@Override
	public String tempScale(MultipartFile file, String basePath, int maxWidth) throws Exception {
		validateFile(file);
		
		String root = appContext.getRoot();
		
		String name = FileNameUtils.genFileName(getExt(file.getOriginalFilename()));
		String path = basePath + "/" + name;
		
		// 存储临时文件
		File temp = new File(root + path);
		checkDirAndCreate(temp);
		
		try {
			file.transferTo(temp);
			
			// 根据临时文件生成略缩图
			String scaleName = FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));
			String dest = root + basePath + "/" + scaleName;
			
			//GMagickUtils.scaleImageByWidth(temp.getAbsolutePath(), dest, maxWidth);
			ImageUtil.scaleImageByWidth(temp.getAbsolutePath(), dest, maxWidth);
			path = basePath + "/" + scaleName;
		} catch (Exception e) {
			throw e;
		} finally {
			if (temp != null) {
				temp.delete();
			}
		}
		return path;
	}
	
	@Override
	public String store(MultipartFile file, String basePath) throws IOException {
		validateFile(file);
		
		String realPath = appContext.getRoot();
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));
		
		File temp = new File(realPath + basePath + path);
		checkDirAndCreate(temp);
		file.transferTo(temp);
		return basePath + path;
	}
	
	@Override
	public String store(File file, String basePath) throws IOException {
		String root = appContext.getRoot();
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getName()));
		
		File dest = new File(root + basePath + path);
		checkDirAndCreate(dest);
		FileUtils.copyDirectory(file, dest);
		return basePath + path;
	}

	@Override
	public String storeScale(MultipartFile file, String basePath, int maxWidth) throws Exception {
		validateFile(file);
		
		String realPath = appContext.getRoot();
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));
		
		File temp = new File(realPath + appContext.getTempDir() + path);
		checkDirAndCreate(temp);
		try {
			file.transferTo(temp);
			
			// 根据临时文件生成略缩图
			String dest = realPath + basePath + path;
			//GMagickUtils.scaleImageByWidth(temp.getAbsolutePath(), dest, maxWidth);
			ImageUtil.scaleImageByWidth(temp.getAbsolutePath(), dest, maxWidth);
		} catch (Exception e) {
			throw e;
		} finally {
			temp.delete();
		}
		
		return basePath + path;
	}

	@Override
	public String storeScale(File file, String basePath, int maxWidth) throws Exception {
		String root = appContext.getRoot();
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getName()));
		
		String dest = root + basePath + path;
		//GMagickUtils.scaleImageByWidth(file.getAbsolutePath(), dest, maxWidth);
		ImageUtil.scaleImageByWidth(file.getAbsolutePath(), dest, maxWidth);
		return basePath + path;
	}

}
