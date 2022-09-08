package com.github.pireba.redminedownloader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.WikiManager;
import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.WikiPage;
import com.taskadapter.redmineapi.bean.WikiPageDetail;

public class WikisDownloader {
	
	private final Path basePath;
	private final Project project;
	
	public WikisDownloader(Project project, Path basePath) {
		this.project = project;
		this.basePath = basePath;
	}
	
	private Path buildDirectoryPath(WikiPage wiki) {
		String name = RedmineDownloader.buildValidFilename(wiki.getTitle());
		
		return this.basePath.resolve(name);
	}
	
	public void start() throws RedmineException {
		WikiManager wikiManager = RedmineDownloader.getInstance().getWikiManager();
		List<WikiPage> wikis = wikiManager.getWikiPagesByProject(this.project.getIdentifier());
		
		for ( int i=0; i<wikis.size(); i++ ) {
			try {
				WikiPage wiki = wikis.get(i);
				
				System.out.println(RedmineDownloader.getLine());
				System.out.println("Current Wiki "+(i+1)+"/"+wikis.size()+"... ");
				System.out.println(RedmineDownloader.getLine());
				System.out.println();
				
				Path wikiPath = this.buildDirectoryPath(wiki);
				System.out.println("Wiki Path: "+wikiPath);
				System.out.println();
				
				System.out.print("Download Wiki File... ");
				WikiPageDetail detail = wikiManager.getWikiPageDetailByProjectAndTitle(this.project.getIdentifier(), wiki.getTitle());
				new WikiDownloader(detail, wikiPath).start();
				System.out.println("OK");
				System.out.println();
				
				System.out.println("Download Wiki Attachments...");
				System.out.println();
				List<Attachment> attachments = detail.getAttachments();
				new AttachmentsDownloader(attachments, wikiPath).start();
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