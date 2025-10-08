package com.Abhinav.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.Abhinav.model.CoinDTO;
import com.Abhinav.request.PromptBody;
import com.Abhinav.response.ApiResponse;
import com.Abhinav.response.FunctionResponse;
import com.Abhinav.service.ChatBotService;
import com.Abhinav.service.ChatBotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController()
@RequestMapping("/chat")
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;

    @GetMapping("/coin/{coinName}")
    public ResponseEntity<CoinDTO> getCoinDetails(@PathVariable String coinName) throws Exception {

        CoinDTO coinDTO = chatBotService.getCoinByName(coinName);
        return new ResponseEntity<>(coinDTO, HttpStatus.OK);
    }

    @PostMapping("/bot")
    public ResponseEntity<String> simpleChat(@RequestBody PromptBody promptBody) {

        String res = chatBotService.simpleChat(promptBody.getPrompt());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PostMapping("/bot/coin")
    public ResponseEntity<ApiResponse> getCoinRealtimeTime(@RequestBody PromptBody promptBody) throws Exception {

        ApiResponse res = chatBotService.getCoinDetails(promptBody.getPrompt());
//        FunctionResponse res = chatBotService.getFunctionResponse(promptBody.getPrompt());

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

//
//    @PostMapping("/bot/coin")
//    public ResponseEntity<ApiResponse> getCoinRealtimeTime(@RequestBody PromptBody promptBody) {
//        try {
//            // Call the simpleChat method which returns JSON as String
//            String res = chatBotService.simpleChat(promptBody.getPrompt());
//
//            // Parse the JSON response
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(res);
//
//            // Navigate to candidates[0].content.parts[0].text
//            String message = root.path("candidates")
//                    .get(0)
//                    .path("content")
//                    .path("parts")
//                    .get(0)
//                    .path("text")
//                    .asText();
//
//            // Create and return ApiResponse
//            ApiResponse apiResponse = new ApiResponse();
//            apiResponse.setMessage(message);
//            apiResponse.setStatus(true);
//
//            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//
//        } catch (Exception e) {
//            // Handle errors gracefully
//            ApiResponse errorResponse = new ApiResponse();
//            errorResponse.setMessage("Error processing the request: " + e.getMessage());
//            errorResponse.setStatus(false);
//
//            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PostMapping("/bot/coin")
//    public ResponseEntity<ApiResponse> getCoinRealtimeTime(@RequestBody PromptBody promptBody) {
//        try {
//            // Call the simpleChat method which returns JSON as String
//            String res = chatBotService.simpleChat(promptBody.getPrompt());
//
//            // Parse the JSON response
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(res);
//
//            // Navigate to candidates[0].content.parts[0].text
//            String message = root.path("candidates")
//                    .get(0)
//                    .path("content")
//                    .path("parts")
//                    .get(0)
//                    .path("text")
//                    .asText();
//
//            // Clean up the message
//            String cleanedMessage = cleanMessage(message);
//
//            // Create and return ApiResponse
//            ApiResponse apiResponse = new ApiResponse();
//            apiResponse.setMessage(cleanedMessage);
//            apiResponse.setStatus(true);
//
//            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//
//        } catch (Exception e) {
//            ApiResponse errorResponse = new ApiResponse();
//            errorResponse.setMessage("Error processing the request: " + e.getMessage());
//            errorResponse.setStatus(false);
//
//            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Utility method to clean the response message
//    private String cleanMessage(String message) {
//        if (message == null) return "";
//
//        // Remove newline characters
//        message = message.replaceAll("\\n", " ");
//
//        // Remove asterisks or markdown formatting
//        message = message.replaceAll("\\*+", "");
//
//        // Remove excessive spaces
//        message = message.replaceAll("\\s{2,}", " ").trim();
//
//        return message;
//    }

}
