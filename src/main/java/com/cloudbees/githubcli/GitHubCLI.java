package com.cloudbees.githubcli;

import com.cloudbees.githubcli.commands.CreatePRCommand;
import com.cloudbees.githubcli.commands.MergePRCommand;

public class GitHubCLI {
	public static void main(String[] args) {
		if (args.length < 1) {
			printUsage();
			return;
		}

		String command = args[0];
		switch (command) {
		case "create-pr":
			if (args.length != 9) {
				printUsage();
				return;
			}
			String repo = args[2];
			String title = args[4];
			String head = args[6];
			String base = args[8];
			new CreatePRCommand(repo, title, head, base).execute();
			break;
		case "merge-pr":
			if (args.length != 5) {
				printUsage();
				return;
			}
			repo = args[2];
			int number = Integer.parseInt(args[4]);
			new MergePRCommand(repo, number).execute();
			break;
		default:
			printUsage();
		}
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("  create-pr -r <repo> -t <title> -h <head> -B <base>");
		System.out.println("  merge-pr -r <repo> -n <number>");
	}
}
