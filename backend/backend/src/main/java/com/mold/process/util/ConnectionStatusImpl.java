package com.mold.process.util;

import java.util.ArrayList;
import java.util.List;

/**
 * RabbitMQ连接鐘舵€佸疄鐜扮被
 */
public class ConnectionStatusImpl implements ConnectionStatus {
    private boolean connected;
    private String host;
    private int port;
    private String username;
    private String connectionDetails;
    private long connectionTime;
    private Exception lastError;
    private List<String> availableExchanges;
    private List<String> availableQueues;
    private long startTime;
    private long endTime;
    
    public ConnectionStatusImpl() {
        this.availableExchanges = new ArrayList<>();
        this.availableQueues = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
    }
    
    public ConnectionStatusImpl(String host, int port, String username) {
        this();
        this.host = host;
        this.port = port;
        this.username = username;
    }
    
    @Override
    public boolean isConnected() {
        return connected;
    }
    
    @Override
    public String getConnectionDetails() {
        StringBuilder details = new StringBuilder();
        details.append("RabbitMQ Connection Status:\n");
        details.append("  Connected: " + connected + "\n");
        details.append("  Host: " + host + "\n");
        details.append("  Port: " + port + "\n");
        details.append("  Username: " + username + "\n");
        details.append("  Connection Time: " + getConnectionTime() + " ms\n");
        
        if (lastError != null) {
            details.append("  Error: " + lastError.getMessage() + "\n");
        }
        
        if (!availableExchanges.isEmpty()) {
            details.append("  Available Exchanges: " + String.join(", ", availableExchanges) + "\n");
        }
        
        if (!availableQueues.isEmpty()) {
            details.append("  Available Queues: " + String.join(", ", availableQueues) + "\n");
        }
        
        return details.toString();
    }
    
    @Override
    public List<String> getAvailableExchanges() {
        return new ArrayList<>(availableExchanges);
    }
    
    @Override
    public List<String> getAvailableQueues() {
        return new ArrayList<>(availableQueues);
    }

    @Override
    public void setAvailableExchanges(List<String> exchanges) {
        if (exchanges == null) {
            this.availableExchanges = new ArrayList<>();
        } else {
            this.availableExchanges = new ArrayList<>(exchanges);
        }
    }

    @Override
    public void setAvailableQueues(List<String> queues) {
        if (queues == null) {
            this.availableQueues = new ArrayList<>();
        } else {
            this.availableQueues = new ArrayList<>(queues);
        }
    }
    
    @Override
    public Exception getLastError() {
        return lastError;
    }
    
    @Override
    public long getConnectionTime() {
        return connectionTime;
    }
    
    @Override
    public void setConnected(boolean connected) {
        this.connected = connected;
        if (connected) {
            this.endTime = System.currentTimeMillis();
            this.connectionTime = endTime - startTime;
        }
    }
    
    @Override
    public void setLastError(Exception error) {
        this.lastError = error;
        this.endTime = System.currentTimeMillis();
        this.connectionTime = endTime - startTime;
    }

    @Override
    public void setConnectionDetails(String details) {
        // store details for diagnostics; keep minimal and reversible
        this.connectionDetails = details;
    }
    
    public void addExchange(String exchange) {
        if (exchange != null && !exchange.isEmpty()) {
            this.availableExchanges.add(exchange);
        }
    }
    
    public void addQueue(String queue) {
        if (queue != null && !queue.isEmpty()) {
            this.availableQueues.add(queue);
        }
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
}
