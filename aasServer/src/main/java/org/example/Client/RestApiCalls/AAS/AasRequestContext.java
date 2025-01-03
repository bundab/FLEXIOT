package org.example.Client.RestApiCalls.AAS;

import org.example.Client.Enums.RequestType;
import org.example.Client.RestApiCalls.BasicRequests.DeleteRequest;
import org.example.Client.RestApiCalls.BasicRequests.GetRequest;
import org.example.Client.RestApiCalls.BasicRequests.PutRequest;
import org.json.JSONObject;

public class AasRequestContext {

    private AasRequestStrategy strategy;

    public AasRequestContext(RequestType requestType) {

        switch (requestType) {
            case Get:
                this.strategy = new GetRequest();
                break;
            case Put:
                this.strategy = new PutRequest();
                break;
            case Delete:
                this.strategy = new DeleteRequest();
                break;
            default:
                //TODO: Throw Exception!
                break;
        }
    }

    public void setStrategy(RequestType requestType) {
        switch (requestType) {
            case Get:
                this.strategy = new GetRequest();
                break;
            case Put:
                this.strategy = new PutRequest();
                break;
            case Delete:
                this.strategy = new DeleteRequest();
                break;
            default:
                //TODO: Throw Exception!
                break;
        }
    }

    public Object executeRequest(String url, JSONObject body) throws Exception {
        return strategy.execute(url, body);
    }
}
