package com.dvir.portwarden

import com.dvir.portwarden.routes.WebSocketRouter
import com.dvir.portwarden.service.PortScanner

import com.dvir.portwarden.service.ProcessManager

import com.dvir.portwarden.service.TcpServer
import org.java_websocket.server.WebSocketServer

fun main(){

    val port = System.getenv("WS_PORT")?.toIntOrNull() ?: 8887
    val server = WebSocketRouter(port)
    server.start()

    println("Portwarden WebSocket server running on port $port")
    readLine()

}