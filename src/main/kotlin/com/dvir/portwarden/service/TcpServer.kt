package com.dvir.portwarden.service

import java.net.ServerSocket

import com.dvir.portwarden.model.Commands

import com.dvir.portwarden.service.PortScanner

import com.dvir.portwarden.service.ProcessManager

class TcpServer {

    fun startServer(serverPort: Int){

        val myServer = ServerSocket(serverPort)
        val commandPortScanner = PortScanner()
        val killThatProcessDude = ProcessManager()
        val ports = commandPortScanner.scanListeningPorts()
        val client = myServer.accept()

        try{

            println("Client connected.")
            val clientMessage = client.getInputStream().bufferedReader().readLine()
            println("Client says: $clientMessage")
            val command = Commands.valueOf(clientMessage)
        
            when (command) {

                FETCH_PORTS -> ports.forEach { portInfo -> println("Port: ${portInfo.port} | PID: ${portInfo.pid} ")}
                KILL_PROCESS -> println("process killing is still not implemented fully")
            }

        } catch (e: Exception) {
            println("You've written a wrong command.")
        }

    }

}