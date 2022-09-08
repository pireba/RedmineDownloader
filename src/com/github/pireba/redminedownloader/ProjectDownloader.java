package com.github.pireba.redminedownloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Project;

public class ProjectDownloader extends Downloader {
	
	private final Path basePath;
	private final Project project;
	
	public ProjectDownloader(Project project, Path basePath) {
		this.project = project;
		this.basePath = basePath;
	}
	
	@Override
	protected Path buildDirectoryPath() {
		return this.basePath;
	}
	
	@Override
	protected Path buildFilePath() {
		String name = RedmineDownloader.buildValidFilename(this.project.getName());
		
		return this.buildDirectoryPath().resolve(name+this.getFileExtension());
	}
	
	@Override
	protected void download() throws IOException, RedmineException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Name: "+this.project.getName()+"\n");
		sb.append("Created On: "+this.project.getCreatedOn()+"\n");
		sb.append("Updated On: "+this.project.getUpdatedOn()+"\n");
		sb.append(RedmineDownloader.getLine()+"\n");
		sb.append(this.project.getDescription());
		
		Files.write(this.buildFilePath(), sb.toString().getBytes());
	}
	
	@Override
	protected String getFileExtension() {
		return ".project";
	}
}