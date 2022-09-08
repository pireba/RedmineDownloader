package com.github.pireba.redminedownloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.taskadapter.redmineapi.AttachmentManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Attachment;

public class AttachmentDownloader extends Downloader {
	
	private final Attachment attachment;
	private final Path basePath;
	
	public AttachmentDownloader(Attachment attachment, Path basePath) {
		this.attachment = attachment;
		this.basePath = basePath;
	}
	
	@Override
	protected Path buildDirectoryPath() {
		return this.basePath;
	}
	
	@Override
	protected Path buildFilePath() {
		String filename = this.attachment.getFileName();
		
		return this.buildDirectoryPath().resolve(filename);
	}
	
	@Override
	protected void download() throws IOException, RedmineException {
		AttachmentManager attachmentManager = RedmineDownloader.getInstance().getAttachmentManager();
		byte[] content = attachmentManager.downloadAttachmentContent(this.attachment);
		
		Files.write(this.buildFilePath(), content);
	}
	
	@Override
	protected String getFileExtension() {
		return null;
	}
}