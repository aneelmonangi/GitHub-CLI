# GitHub CLI Application

This is a command-line interface (CLI) application to interact with GitHub, allowing you to create pull requests programmatically.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Build Instructions](#build-instructions)
- [Setup](#setup)
  - [Setting Up Environment Variables](#setting-up-environment-variables)
    - [On Windows](#on-windows)
    - [On macOS/Linux](#on-macos-linux)
- [Usage](#usage)
  - [Create a Pull Request](#create-a-pull-request)
  - [Merge Request](#merge-request)
- [Contributing](#contributing)
- [License](#license)

## Introduction

This CLI application allows you to create pull requests and merge pull requests on GitHub from the command line. It's designed to be simple and easy to use, making it easier for developers to integrate pull request creation into their workflows.

## Features

- Create pull requests with a single command
- Merge pull requests with a single command
- Specify repository, title, head branch, and base branch
- Authentication via GitHub access token

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven

## Installation

1. Clone the repository:

	git clone https://github.com/aneelmonangi/GitHub-CLI.git

	cd GitHub-CLI

Update config.properties for OWNER(GitHub user)

### Build Instructions
To build the project, use Maven. Run the following command in the project's root directory:
	
	mvn clean package

## Setup

### Setting Up Environment Variables

To authenticate with the GitHub API, you need to set up your GitHub access token as an environment variable. Follow the instructions below based on your operating system.

#### On Windows

1. Open PowerShell.
2. Set Environment Variable in PowerShell Session using the following command:

    $env:GITHUB_TOKEN="=your_access_token"
After setting it, you can verify that it's set by running:
	
	echo $env:GITHUB_TOKEN

#### On macOS/Linux
1. Open your terminal.

2. Set the environment variable using the following command:

	export GITHUB_TOKEN=your_access_token

### Usage
Once you have set up the environment variable, you can use the CLI application to create pull requests.

### Create a Pull Request
To create a pull request, use the following command:

	java -jar target/github-cli-1.0-SNAPSHOT.jar create-pr -r <repository-name> -t "<PR title>" -h <head-branch> -B <base-branch>
For example:
	
	java -jar target/github-cli-1.0-SNAPSHOT.jar create-pr -r GitHub-CLI -t "Add new feature" -h feature-branch -B main
	
### Merge Request
To create merge request, use the following command:

	java -jar target/github-cli-1.0-SNAPSHOT.jar merge-pr -r <repo> -n <number>

For example:
	
	java -jar target/github-cli-1.0-SNAPSHOT.jar merge-pr -r GitHub-CLI -n 1

	
This will compile the project and package it into an executable JAR file located in the target directory.

### Contributing
Contributions are welcome! Please fork the repository and create a pull request with your changes. Make sure to follow the coding standards and include appropriate tests.

### License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

This setup ensures that you only need to use one plugin to handle the creation of an executable JAR, making your `pom.xml` simpler and easier to manage.

	