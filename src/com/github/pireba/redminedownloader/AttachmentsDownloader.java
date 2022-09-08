package com.github.pireba.redminedownloader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Attachment;

public class AttachmentsDownloader {
	
	private final List<Attachment> attachments;
	private final Path basePath;
	
	public AttachmentsDownloader(Collection<Attachment> attachments, Path basePath) {
		this(new ArrayList<>(attachments), basePath);
	}
	
	public AttachmentsDownloader(List<Attachment> attachments, Path basePath) {
		this.attachments = attachments;
		this.basePath = basePath;
	}
	
	public void start() throws RedmineException  {
		for ( int i=0; i<this.attachments.size(); i++ ) {
			try {
				Attachment attachment = this.attachments.get(i);
				
				System.out.print(">>> "+"Download Attachment "+(i+1)+"/"+this.attachments.size()+"... ");
				new AttachmentDownloader(attachment, this.basePath).start();
				System.out.println("OK");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RedmineException e) {
				e.printStackTrace();
			}
		}
	}
}