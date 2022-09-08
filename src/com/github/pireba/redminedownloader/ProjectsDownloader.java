package com.github.pireba.redminedownloader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.taskadapter.redmineapi.NotAuthorizedException;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Project;

public class ProjectsDownloader {
	
	private final Path basePath;
	private final Scanner scanner = new Scanner(System.in);
	
	public ProjectsDownloader(Path basePath) {
		this.basePath = basePath;
	}
	
	private Path buildDirectoryPath(Project project) {
		ProjectManager projectManager = RedmineDownloader.getInstance().getProjectManager();
		Path path = this.basePath;
		
		Project tempProject = project;
		List<String> projects = new ArrayList<>();
		while (true) {
			projects.add(tempProject.getName());
			
			if ( tempProject.getParentId() == null ) {
				break;
			}
			
			try {
				tempProject = projectManager.getProjectById(tempProject.getParentId());
			} catch (RedmineException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		for ( int i=projects.size()-1; i>=0; i-- ) {
			String name = projects.get(i);
			path = path.resolve(RedmineDownloader.buildValidFilename(name));
		}
		
		return path;
	}
	
	public void start() throws RedmineException {
		ProjectManager projectManager = RedmineDownloader.getInstance().getProjectManager();
		List<Project> projects = projectManager.getProjects();
		
		for ( int i=0; i<projects.size(); i++ ) {
			try {
				Project project = projects.get(i);
				
				System.out.println(RedmineDownloader.getLine());
				System.out.println("Current Project "+(i+1)+"/"+projects.size()+": "+project.getName());
				System.out.println(RedmineDownloader.getLine());
				System.out.println();
				
				Path projectPath = this.buildDirectoryPath(project);
				System.out.println("Project Path: "+projectPath);
				System.out.println();
				
				System.out.print("Download Project File... ");
				new ProjectDownloader(project, projectPath).start();
				System.out.println("OK");
				System.out.println();
				
				System.out.println("Download Wiki Pages...");
				new WikisDownloader(project, projectPath).start();
				System.out.println();
				
				System.out.println("Download Issues...");
				new IssuesDownloader(project, projectPath).start();
				
				System.out.println();
				
//				System.out.print("Press Enter... ");
//				this.scanner.nextLine();
			} catch ( IOException e ) {
				System.out.println("Error");
				e.printStackTrace();
			} catch ( NotAuthorizedException e ) {
				System.out.println("Not Authorized");
			}
			
			System.out.println("--------------------");
		}
		
		this.scanner.close();
	}
}