package de.fhg.camel.ids.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class ConnectorRef {
    Server server;
    ServerConnector connector;
    WebsocketComponentServlet servlet;
    MemoryWebsocketStore memoryStore;
    int refCount;

    ConnectorRef(Server server, ServerConnector connector, WebsocketComponentServlet servlet, MemoryWebsocketStore memoryStore) {
        this.server = server;
        this.connector = connector;
        this.servlet = servlet;
        this.memoryStore = memoryStore;
        increment();
    }

    public int increment() {
        return ++refCount;
    }

    public int decrement() {
        return --refCount;
    }

    public int getRefCount() {
        return refCount;
    }
    
    public Server getServer(){
    	return this.server;
    }
    
    public ServerConnector getConnector() {
    	return this.connector;
    }
    
    public WebsocketComponentServlet getServlet() {
    	return this.servlet;
    }
    
    public MemoryWebsocketStore getMemoryStore() {
    	return this.memoryStore;
    }
}
