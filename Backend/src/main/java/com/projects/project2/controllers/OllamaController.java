package com.projects.project2.controllers;

import com.projects.project2.model.User;
import com.projects.project2.model.dto.TaskDto;
import com.projects.project2.repositories.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Tag(name = "Ollama Controller", description = "Controller to interact with the LLM")
public class OllamaController {

    private ChatClient chatClient;
    private TaskRepository repo;

    public OllamaController(OllamaChatModel chatModel, TaskRepository taskRepo){
        this.chatClient = ChatClient.create(chatModel);
        this.repo = taskRepo;

    }

    @Operation(
            summary = "Retrieves a new task",
            description = "Asks the LLM to suggest a new task"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved a task from LLM"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Could not retrieve a task from the LLM",
                    content = @Content
            )
    })
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
