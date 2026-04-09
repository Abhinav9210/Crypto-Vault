package com.Abhinav.service;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.Abhinav.model.CoinDTO;
import com.Abhinav.response.ApiResponse;
import com.Abhinav.response.FunctionResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Map;

@Service
public class ChatBotServiceImpl implements ChatBotService{

    @Value("${gemini.api.key}")
    private String API_KEY;

    private double convertToDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getName());
        }
    }

    public CoinDTO makeApiRequest(String currencyName) throws Exception {
        System.out.println("coin name "+currencyName);
        String url = "https://api.coingecko.com/api/v3/coins/"+currencyName.toLowerCase();

        RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();


            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> responseBody = responseEntity.getBody();
            if (responseBody != null) {
                Map<String, Object> image = (Map<String, Object>) responseBody.get("image");

                Map<String, Object> marketData = (Map<String, Object>) responseBody.get("market_data");

                CoinDTO coinInfo = new CoinDTO();
                coinInfo.setId((String) responseBody.get("id"));
                coinInfo.setSymbol((String) responseBody.get("symbol"));
                coinInfo.setName((String) responseBody.get("name"));
                coinInfo.setImage((String) image.get("large"));
// market data
                coinInfo.setCurrentPrice(convertToDouble(((Map<String, Object>) marketData.get("current_price")).get("usd")));
                coinInfo.setMarketCap(convertToDouble(((Map<String, Object>) marketData.get("market_cap")).get("usd")));
                coinInfo.setMarketCapRank((int) responseBody.get("market_cap_rank"));
                coinInfo.setTotalVolume(convertToDouble(((Map<String, Object>) marketData.get("total_volume")).get("usd")));
                coinInfo.setHigh24h(convertToDouble(((Map<String, Object>) marketData.get("high_24h")).get("usd")));
                coinInfo.setLow24h(convertToDouble(((Map<String, Object>) marketData.get("low_24h")).get("usd")));

                coinInfo.setPriceChange24h(convertToDouble(marketData.get("price_change_24h")) );
                coinInfo.setPriceChangePercentage24h(convertToDouble(marketData.get("price_change_percentage_24h")));
                coinInfo.setMarketCapChange24h(convertToDouble(marketData.get("market_cap_change_24h")));
                coinInfo.setMarketCapChangePercentage24h(convertToDouble( marketData.get("market_cap_change_percentage_24h")));

                coinInfo.setCirculatingSupply(convertToDouble(marketData.get("circulating_supply")));
                coinInfo.setTotalSupply(convertToDouble(marketData.get("total_supply")));

                return coinInfo;

             }
            throw new Exception("coin not found");
//       return null;
    }


//
//public FunctionResponse getFunctionResponse(String prompt) {
//    String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;
//
//    HttpHeaders headers = new HttpHeaders();
//    JSONObject requestBodyJson = new JSONObject()
//            .put("contents", new JSONArray()
//                    .put(new JSONObject()
//                            .put("parts", new JSONArray()
//                                    .put(new JSONObject().put("text", prompt))
//                            )
//                    )
//            )
//            .put("tools", new JSONArray()
//                    .put(new JSONObject()
//                            .put("functionDeclarations", new JSONArray()
//                                    .put(new JSONObject()
//                                            .put("name", "getCoinDetails")
//                                            .put("description", "Get the coin details from given currency object")
//                                            .put("parameters", new JSONObject()
//                                                    .put("type", "OBJECT")
//                                                    .put("properties", new JSONObject()
//                                                            .put("currencyName", new JSONObject()
//                                                                    .put("type", "STRING")
//                                                                    .put("description", "The currency name, id, symbol.")
//                                                            )
//                                                            .put("currencyData", new JSONObject()
//                                                                    .put("type", "STRING")
//                                                                    .put("description", "Currency Data...")
//                                                            )
//                                                    )
//                                                    .put("required", new JSONArray()
//                                                            .put("currencyName")
//                                                            .put("currencyData")
//                                                    )
//                                            )
//                                    )
//                            )
//                    )
//            );
//
//    HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson.toString(), headers);
//    RestTemplate restTemplate = new RestTemplate();
//    ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);
//
//    String responseBody = response.getBody();
//    System.out.println("Gemini Raw Response:\n" + responseBody);
//
//    JSONObject jsonObject = new JSONObject(responseBody);
//    JSONArray candidates = jsonObject.getJSONArray("candidates");
//    JSONObject firstCandidate = candidates.getJSONObject(0);
//    JSONObject content = firstCandidate.getJSONObject("content");
//    JSONArray parts = content.getJSONArray("parts");
//    JSONObject firstPart = parts.getJSONObject(0);
//
//    if (!firstPart.has("functionCall")) {
//        System.out.println("No functionCall found in Gemini response.");
//        throw new RuntimeException("functionCall not found. Check your prompt or tool setup.");
//    }
//
//    JSONObject functionCall = firstPart.getJSONObject("functionCall");
//
//    // Extract function name and arguments
//    String functionName = functionCall.getString("name");
//    JSONObject args = functionCall.getJSONObject("args");
//    String currencyName = args.getString("currencyName");
//    String currencyData = args.getString("currencyData");
//
//    System.out.println(functionName + " ------- " + currencyName + " ----- " + currencyData);
//
//    FunctionResponse res = new FunctionResponse();
//    res.setFunctionName(functionName);
//    res.setCurrencyName(currencyName);
//    res.setCurrencyData(currencyData);
//
//    return res;
//}
//
public FunctionResponse getFunctionResponse(String prompt) {
    String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Add guiding instruction
    String systemInstruction = "You are a crypto assistant. "
            + "When the user asks about cryptocurrency price, market cap, or details, "
            + "you MUST call the function getCoinDetails with the correct currencyName "
//            + "(like 'bitcoin', 'ethereum', 'dogecoin')."
            ;

    JSONObject requestBodyJson = new JSONObject()
            .put("contents", new JSONArray()
                    .put(new JSONObject()
                            .put("parts", new JSONArray()
                                    .put(new JSONObject().put("text", systemInstruction + " User asked: " + prompt))
                            )
                    )
            )
            .put("tools", new JSONArray()
                    .put(new JSONObject()
                            .put("functionDeclarations", new JSONArray()
                                    .put(new JSONObject()
                                            .put("name", "getCoinDetails")
                                            .put("description", "Get the coin details for a given currency name.")
                                            .put("parameters", new JSONObject()
                                                    .put("type", "OBJECT")
                                                    .put("properties", new JSONObject()
                                                            .put("currencyName", new JSONObject()
                                                                    .put("type", "STRING")
                                                                    .put("description", "The cryptocurrency name, id, or symbol (e.g. bitcoin, ethereum, dogecoin).")
                                                            )
                                                    )
                                                    .put("required", new JSONArray()
                                                            .put("currencyName") // ✅ only currencyName required
                                                    )
                                            )
                                    )
                            )
                    )
            );

    HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson.toString(), headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);

    String responseBody = response.getBody();
    System.out.println("Gemini Raw Response:\n" + responseBody);

    JSONObject jsonObject = new JSONObject(responseBody);
    JSONArray candidates = jsonObject.getJSONArray("candidates");
    JSONObject firstCandidate = candidates.getJSONObject(0);
    JSONObject content = firstCandidate.getJSONObject("content");
    JSONArray parts = content.getJSONArray("parts");
    JSONObject firstPart = parts.getJSONObject(0);

//    if (!firstPart.has("functionCall")) {
//        throw new RuntimeException("functionCall not found. Gemini did not trigger tool usage. Try improving prompt.");
//    }
    if (!firstPart.has("functionCall")) {
        System.out.println("No functionCall found, falling back to simpleChat...");
//        String simpleResponse = simpleChat(prompt);
//
        FunctionResponse fallback = new FunctionResponse();
        fallback.setFunctionName("simpleChat");
//        fallback.setCurrencyName("");
//        fallback.setCurrencyData(simpleResponse);
//
//        return fallback;
        ReadContext ctx = JsonPath.parse(response.getBody());
        String text = ctx.read("$.candidates[0].content.parts[0].text");

//        ApiResponse ans = new ApiResponse();
        fallback.setCurrencyData(text);

        return fallback;
    }

    JSONObject functionCall = firstPart.getJSONObject("functionCall");

    // Extract function name and arguments
    String functionName = functionCall.getString("name");
    JSONObject args = functionCall.getJSONObject("args");
    String currencyName = args.getString("currencyName");

    System.out.println("FunctionCall Triggered: " + functionName + " | Currency: " + currencyName);

    FunctionResponse res = new FunctionResponse();
    res.setFunctionName(functionName);
    res.setCurrencyName(currencyName);
    res.setCurrencyData(""); // optional, can be filled later

    return res;
}

    @Override
    public ApiResponse getCoinDetails(String prompt) throws Exception {

        FunctionResponse res = getFunctionResponse(prompt);

        // ✅ Agar Gemini functionCall nahi bhejta toh sidha simpleChat ka response
        if ("simpleChat".equals(res.getFunctionName())) {
            ApiResponse ans = new ApiResponse();
            ans.setMessage(res.getCurrencyData()); // already simpleChat ka response hai
            return ans;
        }

        // 👇 Normal CoinGecko flow
        String apiResponse = makeApiRequest(res.getCurrencyName()).toString();

        String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construct request body using JSONObjects
        JSONObject userPart = new JSONObject()
                .put("role", "user")
                .put("parts", new JSONArray()
                        .put(new JSONObject().put("text", prompt)));

        JSONObject modelPart = new JSONObject()
                .put("role", "model")
                .put("parts", new JSONArray()
                        .put(new JSONObject()
                                .put("functionCall", new JSONObject()
                                        .put("name", "getCoinDetails")
                                        .put("args", new JSONObject()
                                                .put("currencyName", res.getCurrencyName())
                                                .put("currencyData", res.getCurrencyData())
                                        )
                                )
                        )
                );

        JSONObject functionPart = new JSONObject()
                .put("role", "function")
                .put("parts", new JSONArray()
                        .put(new JSONObject()
                                .put("functionResponse", new JSONObject()
                                        .put("name", "getCoinDetails")
                                        .put("response", new JSONObject()
                                                .put("name", "getCoinDetails")
                                                .put("content", new JSONObject(apiResponse)) // Ensure apiResponse is valid JSON
                                        )
                                )
                        )
                );

        // Combine all parts into contents array
        JSONArray contents = new JSONArray()
                .put(userPart)
                .put(modelPart)
                .put(functionPart);

        // Build function declaration
        JSONObject getCoinDetailsFunc = new JSONObject()
                .put("name", "getCoinDetails")
                .put("description", "Get crypto currency data from given currency object.")
                .put("parameters", new JSONObject()
                        .put("type", "OBJECT")
                        .put("properties", new JSONObject()
                                .put("currencyName", new JSONObject()
                                        .put("type", "STRING")
                                        .put("description", "The currency Name, id, symbol ."))
                                .put("currencyData", new JSONObject()
                                        .put("type", "STRING")
                                        .put("description", "The currency data id, symbol, current price, image, market cap extra... "))
                        )
                        .put("required", new JSONArray()
                                .put("currencyName")
                                .put("currencyData"))
                );

        // Additional tools (optional other functions)
        JSONArray functionDeclarations = new JSONArray()
                .put(getCoinDetailsFunc);
        // You can also add find_theaters or get_showtimes here if needed

        JSONObject tools = new JSONObject()
                .put("functionDeclarations", functionDeclarations);

        // Final body
        JSONObject requestBody = new JSONObject()
                .put("contents", contents)
                .put("tools", new JSONArray().put(tools));

        // Send request
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, request, String.class);

        System.out.println("Response: " + response.getBody());

        // Parse Gemini response
        ReadContext ctx = JsonPath.parse(response.getBody());
        String text = ctx.read("$.candidates[0].content.parts[0].text");

        ApiResponse ans = new ApiResponse();
        ans.setMessage(text);

        return ans;
    }

    @Override
    public CoinDTO getCoinByName(String coinName) throws Exception {
        return this.makeApiRequest(coinName);
//        return null;
    }

    @Override
    public String simpleChat(String prompt) {

        String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construct the request body using JSONObject
        JSONObject requestBody = new JSONObject();
        JSONArray contentsArray = new JSONArray();
        JSONObject contentsObject = new JSONObject();
        JSONArray partsArray = new JSONArray();
        JSONObject textObject = new JSONObject();
        textObject.put("text", prompt);
        partsArray.put(textObject);
        contentsObject.put("parts", partsArray);
        contentsArray.put(contentsObject);
        requestBody.put("contents", contentsArray);

        // Create the HTTP entity with headers and request body
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

        // Make the POST request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);


        String responseBody = response.getBody();

        System.out.println("Response Body: " + responseBody);
//import org.json.JSONObject;

        JSONObject json = new JSONObject(responseBody);
        String text = json.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");

        System.out.println(text);

        return text;
    }


}
