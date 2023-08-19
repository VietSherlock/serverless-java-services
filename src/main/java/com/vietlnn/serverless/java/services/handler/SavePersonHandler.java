package com.vietlnn.serverless.java.services.handler;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.vietlnn.serverless.java.services.request.PersonRequest;
import com.vietlnn.serverless.java.services.response.PersonResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SavePersonHandler implements RequestHandler<PersonRequest, PersonResponse> {

    private AmazonDynamoDB amazonDynamoDB;
    private String DYNAMODB_TABLE_NAME = "Person";
    private Regions REGION = Regions.AP_SOUTHEAST_2;

    @Override
    public PersonResponse handleRequest(PersonRequest personRequest, Context context) {

        this.initDynamoDbClient();
        persistData(personRequest);

        PersonResponse personResponse = new PersonResponse();
        personResponse.setMessage("Saved Successfully!!!");
        return personResponse;
    }

    private void persistData(PersonRequest personRequest) throws ConditionalCheckFailedException {

        Map<String, AttributeValue> attributesMap = new HashMap<>();

        attributesMap.put("id", new AttributeValue(UUID.randomUUID().toString()));
        attributesMap.put("firstName", new AttributeValue(personRequest.getFirstName()));
        attributesMap.put("lastName", new AttributeValue(personRequest.getLastName()));
        attributesMap.put("age", new AttributeValue(String.valueOf(personRequest.getAge())));
        attributesMap.put("address", new AttributeValue(personRequest.getAddress()));

        amazonDynamoDB.putItem(DYNAMODB_TABLE_NAME, attributesMap);
    }

    private void initDynamoDbClient() {
        this.amazonDynamoDB = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(REGION)
                .build();
    }
}
