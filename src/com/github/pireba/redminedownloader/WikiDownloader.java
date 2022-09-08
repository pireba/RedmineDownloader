package com.github.pireba.redminedownloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.taskadapter.redmineapi.bean.WikiPageDetail;

public class WikiDownloader extends Downloader {
	
	private final Path basePath;
	private final WikiPageDetail wiki;
	
	public WikiDownloader(WikiPageDetail wiki, Path basePath) {
		this.basePath = basePath;
		this.wiki = wiki;
	}
	
	@Override
	protected Path buildDirectoryPath() {
		return this.basePath;
	}
	
	@Override
	protected Path buildFilePath() {
		String name = RedmineDownloader.buildValidFilename(this.wiki.getTitle());
		
		return this.buildDirectoryPath().resolve(name+this.getFileExtension());
	}
	
	@Override
	protected void download() throws IOException {
		String text = this.wiki.getText();
		
		Files.write(this.buildFilePath(), text.getBytes());
	}
	
	@Override
	protected String getFileExtension() {
		return ".wiki";
	}
}