package com.github.pireba.redminedownloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.taskadapter.redmineapi.bean.Issue;

public class IssueDownloader extends Downloader {
	
	private final Issue issue;
	private final Path basePath;
	
	public IssueDownloader(Issue issue, Path basePath) {
		this.issue = issue;
		this.basePath = basePath;
	}
	
	@Override
	protected Path buildDirectoryPath() {
		return this.basePath;
	}

	@Override
	protected Path buildFilePath() {
		String name = RedmineDownloader.buildValidFilename(this.issue.getSubject());
		
		return this.buildDirectoryPath().resolve(name+this.getFileExtension());
	}

	@Override
	protected void download() throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Subject: "+this.issue.getSubject()+"\n");
		sb.append("Author: "+this.issue.getAuthorName()+"\n");
		sb.append("Assignee: "+this.issue.getAssigneeName()+"\n");
		sb.append("Updated On: "+this.issue.getUpdatedOn()+"\n");
		sb.append("Created On: "+this.issue.getCreatedOn()+"\n");
		sb.append(RedmineDownloader.getLine()+"\n");
		sb.append(this.issue.getDescription());
		
		Files.write(this.buildFilePath(), sb.toString().getBytes());
	}
	
	@Override
	protected String getFileExtension() {
		return ".issue";
	}
}