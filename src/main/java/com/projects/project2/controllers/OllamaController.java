package com.projects.project2.controllers;

import com.projects.project2.model.Task;
import com.projects.project2.model.User;
import com.projects.project2.model.dto.ReturnTaskDto;
import com.projects.project2.model.dto.TaskDto;
import com.projects.project2.repositories.TaskRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class OllamaController {

    private ChatClient chatClient;
    @Autowired
    private TaskRepository repo;

    public OllamaController(OllamaChatModel chatModel){
        this.chatClient = ChatClient.create(chatModel);
    }

    @GetMapping("/message")
    public ResponseEntity<String> suggestNewTask(@AuthenticationPrincipal User user){
        List<TaskDto> tasks = repo.findByUser(user).stream().map(task -> new TaskDto(task.getTitle(), task.getDescription(), task.getDueDate().toString(), task.isCompleted())).toList();
        String taskListText = tasks.stream()
                .map(t -> String.format("- %s | %s | Due: %s | %s",
                        t.title(),
                        t.description() != null ? t.description() : "no description",
                        t.dueDate(),
                        t.completed() ? "COMPLETED" : "PENDING"))
                .collect(Collectors.joining("\n"));

        String descPrompt = """
            You are a helpful personal productivity assistant.
            Your task is to suggest **exactly one** new, realistic task that would fit naturally 
            into the user's existing todo list.
            
            Rules you MUST follow strictly:
            - Look at patterns, themes, priorities, time sensitivity and categories in the existing tasks
            - Make the new task similar in style, level of detail and scope
            - Do NOT repeat or very similar to existing tasks
            - Output format MUST be exactly this CSV-like single line, nothing else:
              title,description,duedate
            - duedate format: use YYYY-MM-DD or leave empty if no logical due date
            - description can be empty if not needed
            - No explanations, no extra text, no markdown, no lists - ONLY the one line
            
            Existing tasks:
            %s
            
            New suggested task:
            """.formatted(taskListText);

        ChatResponse chatResponse = chatClient
                .prompt(descPrompt)
                .call()
                .chatResponse();

        System.out.println(chatResponse.getMetadata().getModel());

        String response = chatResponse.getResult().getOutput().getText();

        return ResponseEntity.ok(response);
    }

}
