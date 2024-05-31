package com.cloudbees.githubcli.services;

import com.cloudbees.githubcli.utils.ConfigLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitHubService {
	private static final String API_URL = "https://api.github.com";
	private final ObjectMapper objectMapper;
	private final String token;
	private String OWNER;

	public GitHubService() {
		this.objectMapper = new ObjectMapper();
		this.OWNER = ConfigLoader.getInstance().getProperty("OWNER");
		// Ensure the token is passed correctly
		this.token = System.getenv("GITHUB_TOKEN");
		if (token == null || token.isEmpty()) {
			System.err.println("GitHub token is required. Set it as an environment variable GITHUB_TOKEN.");
			System.exit(1);
		}

	}

	public String createPullRequest(String repo, String title, String body, String head, String base) {
		String url = String.format("%s/repos/%s/%s/pulls", API_URL, OWNER, repo);
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("title", title);
		requestBody.put("body", body);
		requestBody.put("head", head);
		requestBody.put("base", base);

		try {
			return verifyAndMakePullRequest(repo, requestBody); // makePostRequest(url, requestBody);
		} catch (IOException e) {
			return "Error making request: " + e.getMessage();
		}
	}

	public String mergePullRequest(String repo, int number) {
		String url = String.format("%s/repos/%s/%s/pulls/%d/merge", API_URL, OWNER, repo, number);

		try {
			return makePutRequest(url);
		} catch (IOException e) {
			return "Error making request: " + e.getMessage();
		}
	}

	private String makePostRequest(String urlString, Map<String, String> requestBody, String method)
			throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		connection.setRequestMethod(method);
		connection.setRequestProperty("X-HTTP-Method-Override", method);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Authorization", "Bearer " + token);
		connection.setRequestProperty("Content-Type", "application/json; utf-8");
		connection.setRequestProperty("Accept", "application/vnd.github+json");
		connection.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");
		connection.setDoOutput(true);

		String jsonInputString = objectMapper.writeValueAsString(requestBody);

		try (OutputStream os = connection.getOutputStream()) {
			byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
			os.write(input, 0, input.length);
		}

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
			StringBuilder response = new StringBuilder();
			String responseLine;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			return response.toString();
		}
	}

	private String makePutRequest(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Authorization", "token " + token);
		connection.setRequestProperty("Content-Type", "application/json; utf-8");
		connection.setDoOutput(true);

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
			StringBuilder response = new StringBuilder();
			String responseLine;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			return response.toString();
		}
	}

	private String verifyAndMakePullRequest(String repo, Map<String, String> requestBody) throws IOException {
		String urlString = String.format("https://api.github.com/repos/%s/%s/pulls", OWNER, repo);
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Authorization", "Bearer " + token);
		connection.setRequestProperty("Content-Type", "application/json; utf-8");
		connection.setRequestProperty("Accept", "application/vnd.github+json");
		connection.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");
		connection.setDoOutput(true);

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
			StringBuilder response = new StringBuilder();
			String responseLine;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}

//			System.out.println(response.toString());
			List<Map> mapper = objectMapper.readValue(response.toString(), List.class);
			if(mapper.size()>0) {
				int number = (int) mapper.get(0).get("number");
				requestBody.put("state", "open");
				String updatedUrl = urlString + "/" + number;
				System.out.println(updatedUrl);
				return makePostRequest(updatedUrl, requestBody, "PATCH");
			} else {
				String prUrl = String.format("%s/repos/%s/%s/pulls", API_URL, OWNER, repo);
				return makePostRequest(prUrl, requestBody, "POST");
			}
			
			/*
			 * if (state.equals("open")) { requestBody.put("state", "open"); return
			 * makePostRequest(urlString + "/" + number, requestBody, "PATCH"); } else {
			 * String prUrl = String.format("%s/repos/%s/%s/pulls", API_URL, OWNER, repo);
			 * return makePostRequest(prUrl, requestBody, "POST"); }
			 */
		}
	}
}
