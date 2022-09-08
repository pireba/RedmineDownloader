package com.github.pireba.redminedownloader;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import com.taskadapter.redmineapi.AttachmentManager;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.WikiManager;

public class RedmineDownloader {
	
	private static RedmineDownloader instance;
	
	private final RedmineManager redmineManager;
	
	private RedmineDownloader(String redmineUrl, String apiKey) {
		this.redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiKey);
	}
	
	private RedmineDownloader(String redmineUrl, String apiKey, String proxyAddress, int proxyPort) {
		this.redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiKey, this.createProxy(proxyAddress, proxyPort));
	}
	
	public static RedmineDownloader createInstance(String redmineUrl, String apiKey) {
		instance = new RedmineDownloader(redmineUrl, apiKey);
		return instance;
	}
	
	public static RedmineDownloader createInstance(String redmineUrl, String apiKey, String proxyAddress, int proxyPort) {
		instance = new RedmineDownloader(redmineUrl, apiKey, proxyAddress, proxyPort);
		return instance;
	}
	
	public static RedmineDownloader getInstance() {
		return instance;
	}
	
	private HttpClient createProxy(String proxyAddress, int proxyPort) {
		HttpHost proxy = new HttpHost(proxyAddress, proxyPort);
		DefaultProxyRoutePlanner proxyRoute = new DefaultProxyRoutePlanner(proxy);
		return HttpClients.custom().setRoutePlanner(proxyRoute).build();
	}
	
	public static String buildValidFilename(String string) {
		return string.replaceAll("[^a-zäöüßA-ZÄÖÜ0-9]", "_");
	}
	
	public static String getLine() {
		return "--------------------------------------------------";
	}
	
	public AttachmentManager getAttachmentManager() {
		return this.redmineManager.getAttachmentManager();
	}
	
	public IssueManager getIssueManager() {
		return this.redmineManager.getIssueManager();
	}
	
	public ProjectManager getProjectManager() {
		return this.redmineManager.getProjectManager();
	}
	
	public WikiManager getWikiManager() {
		return this.redmineManager.getWikiManager();
	}
}