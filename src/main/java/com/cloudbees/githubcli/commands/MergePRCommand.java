package com.cloudbees.githubcli.commands;

import com.cloudbees.githubcli.services.GitHubService;

public class MergePRCommand {
	private final String repo;
	private final int number;

	public MergePRCommand(String repo, int number) {
		this.repo = repo;
		this.number = number;
	}

	public void execute() {
		GitHubService gitHubService = new GitHubService();
		String response = gitHubService.mergePullRequest(repo, number);
		System.out.println(response);
	}
}
