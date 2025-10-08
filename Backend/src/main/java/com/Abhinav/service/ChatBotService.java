package com.Abhinav.service;

import com.Abhinav.model.CoinDTO;
import com.Abhinav.response.ApiResponse;
import com.Abhinav.response.FunctionResponse;

public interface ChatBotService {
    ApiResponse getCoinDetails(String coinName) throws Exception;

    CoinDTO getCoinByName(String coinName) throws Exception;

    String simpleChat(String prompt);

    FunctionResponse getFunctionResponse(String prompt);
}

