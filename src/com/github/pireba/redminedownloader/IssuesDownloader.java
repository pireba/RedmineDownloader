package com.github.pireba.redminedownloader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Project;

public class IssuesDownloader {
	
	private final Path basePath;
	private final Project project;
	
	public IssuesDownloader(Project project, Path basePath) {
		this.project = project;
		this.basePath = basePath;
	}
	
	private Path buildDirectoryPath(Issue issue) {
		int id = issue.getId();
		String name = RedmineDownloader.buildValidFilename(String.valueOf(id));
		
		return this.basePath.resolve("Tickets").resolve(name);
	}
	
	public void start() throws RedmineException {
		IssueManager issueManager = RedmineDownloader.getInstance().getIssueManager();
		List<Issue> issues = issueManager.getIssues(this.project.getIdentifier(), null, Include.attachments);
		
		for ( int i=0; i<issues.size(); i++ ) {
			try {
				Issue issue = issues.get(i);
				
				System.out.println();
				System.out.println(RedmineDownloader.getLine());
				System.out.println("Current Issue "+(i+1)+"/"+issues.size()+"... ");
				System.out.println(RedmineDownloader.getLine());
				System.out.println();
				
				Path issuePath = this.buildDirectoryPath(issue);
				System.out.println("Issue Path: "+issuePath);
				System.out.println();
				
				System.out.print("Download Issue File... ");
				new IssueDownloader(issue, issuePath).start();
				System.out.println("OK");
				System.out.println();
				
				System.out.println("Download Issue Attachments...");
				System.out.println();
				Collection<Attachment> attachments = issue.getAttachments();
				new AttachmentsDownloader(attachments, issuePath).start();
			} catch (IOException e) {
				System.out.println("Error");
				e.printStackTrace();
			} catch (RedmineException e) {
				System.out.println("Error");
				e.printStackTrace();
			}
		}
	}
}