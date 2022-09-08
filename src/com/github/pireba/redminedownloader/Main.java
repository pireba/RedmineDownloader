package com.github.pireba.redminedownloader;

import java.nio.file.Path;

import com.taskadapter.redmineapi.RedmineException;

public class Main {
	
	public static void main(String[] args) {
		if ( args.length < 3 ) {
			printUsage();
		}
		
		Path basePath = Path.of(args[0]);
		String redmineURL = args[1];
		String redmineAPI = args[2];
		
		if ( args.length == 5 ) {
			String proxyAddres = args[3];
			int proxyPort = Integer.valueOf(args[4]);
			RedmineDownloader.createInstance(redmineURL, redmineAPI, proxyAddres, proxyPort);
		} else {
			RedmineDownloader.createInstance(redmineURL, redmineAPI);
		}
		
		try {
			new ProjectsDownloader(basePath).start();
		} catch (RedmineException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private static void printUsage() {
		printUsage(null);
	}
	
	private static void printUsage(String message) {
		StringBuilder sb = new StringBuilder();
		
		if ( message != null && ! message.isEmpty() ) {
			sb.append(message+"\n");
			sb.append("\n");
		}
		
		sb.append("ANWENDUNG\n");
		sb.append("    java -jar redminedownloader.jar [base path] [redmine url] [redmine api key] [proxy address] [proxy port]\n");
		sb.append("\n");
		sb.append("    TODO");
		sb.append("\n");
		sb.append("ARGUMENTS:\n");
		sb.append("    [base path]          \n");
		sb.append("    [redmine url]        \n");
		sb.append("    [redmine api key]    \n");
		sb.append("    [proxy address]      \n");
		sb.append("    [proxy port]         \n");
		sb.append("\n");
		sb.append("EXAMPLE:\n");
		sb.append("    java -jar redminedownloader.jar /home/phillip/Redmine http://redmine.example.lan ABCsecret123 192.168.10.36 3128");
		
		System.err.println(sb.toString());
		System.exit(1);
	}
}