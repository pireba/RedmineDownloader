package com.github.pireba.redminedownloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.taskadapter.redmineapi.RedmineException;

public abstract class Downloader {
	
	protected void createDirectory() throws IOException {
		Files.createDirectories(this.buildDirectoryPath());
	}
	
	public void start() throws IOException, RedmineException {
		this.createDirectory();
		this.download();
	}
	
	protected abstract Path buildDirectoryPath();
	protected abstract Path buildFilePath();
	protected abstract void download() throws IOException, RedmineException;
	protected abstract String getFileExtension();
}