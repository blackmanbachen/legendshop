/*
 * 
 * LegendShop 多用户商城系统
 * 
 *  版权所有,并保留所有权利。
 * 
 */
package com.legendshop.business.common.fck;

import java.io.IOException;
import java.io.InputStream;

import net.fckeditor.connector.exception.InvalidCurrentFolderException;
import net.fckeditor.connector.exception.WriteException;
import net.fckeditor.connector.impl.LocalConnector;
import net.fckeditor.handlers.ResourceType;
import net.fckeditor.requestcycle.ThreadLocalData;

import com.legendshop.core.UserManager;
import com.legendshop.core.constant.ParameterEnum;
import com.legendshop.core.helper.FileProcessor;
import com.legendshop.core.helper.PropertiesUtil;

/**
 * 文件上传的操作在ContextConnector类中，这里只是对其进行重命名操作，其实完全可以重新自己写上传的代码
 * 
 * LegendShop 版权所有 2009-2011,并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得LegendShop商业授权之前，您不能将本软件应用于商业用途，否则LegendShop将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.legendesign.net
 * ----------------------------------------------------------------------------
 */
public class ContextConnector extends LocalConnector {

	// 此方法中可以对文件重命名
	/* (non-Javadoc)
	 * @see net.fckeditor.connector.impl.AbstractLocalFileSystemConnector#fileUpload(net.fckeditor.handlers.ResourceType, java.lang.String, java.lang.String, java.io.InputStream)
	 */
	@Override
	public String fileUpload(ResourceType type, String currentFolder, String fileName, InputStream inputStream)
			throws InvalidCurrentFolderException, WriteException {
		String name = fileName;
		// 重命名操作在这里进行
		try {
			int size = inputStream.available();
			if ((size < 0) || (size > PropertiesUtil.getObject(ParameterEnum.MAX_FILE_SIZE, Long.class))) {
				throw new RuntimeException("File is 0 or File Size exceeded MAX_FILE_SIZE: " + size);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String userName = UserManager.getUsername(ThreadLocalData.getRequest());
		if (userName == null) {
			throw new RuntimeException("You are no logging!");
		}

		String path = new StringBuffer().append(PropertiesUtil.getBigFilesAbsolutePath()).append("/").append(userName)
				.append("/fckeditor/image/").append(currentFolder).toString();
		FileProcessor.uploadFile(inputStream, path, "", fileName, true, false);
		return name;
	}
}