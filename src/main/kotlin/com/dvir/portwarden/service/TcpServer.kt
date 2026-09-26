package com.dvir.portwarden.service

import java.net.ServerSocket

import com.dvir.portwarden.model.Commands

import java.io.BufferedInputStream

class TcpServer {

    fun startServer(serverPort: Int){

        val myServer = ServerSocket(serverPort)
        val commandPortScanner = PortScanner()
        val killThatProcessDude = ProcessManager()
        val client = myServer.accept()

        try {
            while (true) {

                val ports = commandPortScanner.scanListeningPorts()
                val clientReader = client.getInputStream().bufferedReader()
                val clientWriter = java.io.PrintWriter(client.getOutputStream(), true)
                val clientMessage = clientReader.readLine().trim().uppercase()
                clientWriter.println("Client says: $clientMessage")
                val command = Commands.valueOf(clientMessage)

                when (command) {

                    Commands.FETCH_PORTS -> {
                        ports.forEach { portInfo -> clientWriter.println("Port: ${portInfo.port} | PID: ${portInfo.pid} ") }
                    }

                    Commands.KILL_PROCESS -> {
                        clientWriter.println("Kill port: ")
                        val portInputStr = clientReader.readLine()
                        val portInput: Int? = portInputStr?.toIntOrNull()
                        var targetPid: Long = -1L
                        ports.forEach { portInfo ->
                            if (portInfo.port == portInput) {
                                targetPid = portInfo.pid
                            }
                        }
                        if (portInput == null || targetPid == -1L) {
                            clientWriter.println("No process listening on that port.")
                        } else if (!commandPortScanner.isKillable(portInput, targetPid)) {
                            clientWriter.println("Port owner changed, try again.")
                        } else if (!killThatProcessDude.killProcess(targetPid)) {
                            clientWriter.println("Couldn't kill process (permission denied?)")
                        } else {
                            clientWriter.println("Process $targetPid killed successfully.")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("You've written a wrong command.")
        }

    }

}