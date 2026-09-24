package com.dvir.portwarden.service

import java.net.ServerSocket

import com.dvir.portwarden.model.Commands

class TcpServer {

    fun startServer(port: Int){

        val myServer = ServerSocket(port)
        val client = myServer.accept()
        println("Client connected.")
        val clientMessage = client.getInputStream().bufferedReader().readLine()
        println("Client says: $clientMessage")
        try {

        val command = Commands.valueOf(clientMessage)

        } catch {
            println("You've written a wrong command.")
        }

    }

}