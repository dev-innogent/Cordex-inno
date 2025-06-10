package com.systemmind;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Very simple helper that calls a free Google AI model to
 * translate a natural language prompt into a system command. The
 * expected JSON response should contain fields "description" and
 * "command" (array of strings).
 *
 * This is only an example; error handling and authentication need
 * to be adapted for a real project.
 */
public class AICommandService {
    private final String apiKey;

    public AICommandService(String apiKey) {
        this.apiKey = apiKey;
    }

    public Command requestCommand(String prompt) throws IOException, InterruptedException {
        JsonObject json = new JsonObject();
        JsonObject promptObj = new JsonObject();
        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();
        message.addProperty("author", "user");
        message.addProperty("content", prompt);
        messages.add(message);
        promptObj.add("messages", messages);
        json.add("prompt", promptObj);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta2/models/chat-bison-001:generateMessage?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("API error: " + response.body());
        }
        // Expecting the assistant to respond with a JSON block containing description and command
        JsonObject obj = JsonParser.parseString(response.body())
                .getAsJsonObject()
                .getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content");
        String contentText = obj.getAsJsonArray("parts").get(0).getAsJsonObject().get("text").getAsString().trim();
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
