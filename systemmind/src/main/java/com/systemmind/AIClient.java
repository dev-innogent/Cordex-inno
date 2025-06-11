package com.systemmind;

import com.google.gson.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Simple helper that calls the OpenRouter chat completion API to
 * convert a natural language prompt into a command.
 */
public class AIClient {
    private final String apiKey;

    public AIClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public Command requestCommand(String prompt) throws IOException, InterruptedException {
        JsonObject json = new JsonObject();
        json.addProperty("model", "mistralai/mistral-7b-instruct");

        JsonArray messages = new JsonArray();
        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", prompt);
        messages.add(userMsg);
        json.add("messages", messages);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://openrouter.ai/api/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("API error: " + response.body());
        }

        JsonObject resp = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject messageObj = resp.getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message");

        String contentText = messageObj.get("content").getAsString().trim();
        // Expect assistant to return JSON string with description and command array
        JsonObject cmdObj = JsonParser.parseString(contentText).getAsJsonObject();
        String desc = cmdObj.get("description").getAsString();
        JsonArray cmdArray = cmdObj.getAsJsonArray("command");
        String[] cmd = new String[cmdArray.size()];
        for (int i = 0; i < cmdArray.size(); i++) {
            cmd[i] = cmdArray.get(i).getAsString();
        }
        return new Command(desc, cmd);
    }
}
