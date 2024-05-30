package com.cloudbees.githubcli.commands;

import com.cloudbees.githubcli.services.GitHubService;

public class CreatePRCommand {
	private final String repo;
	private final String title;
	private final String head;
	private final String base;

	public CreatePRCommand(String repo, String title, String head, String base) {
		this.repo = repo;
		this.title = title;
		this.head = head;
		this.base = base;
	}

	public void execute() {
		GitHubService gitHubService = new GitHubService();
		String response = gitHubService.createPullRequest(repo, title, "Please pull these awesome changes in!", head,
				base);
		System.out.println(response);
	}
}
