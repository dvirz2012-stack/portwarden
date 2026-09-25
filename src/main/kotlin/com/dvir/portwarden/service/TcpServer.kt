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
                var clientReader = client.getInputStream().bufferedReader()
                var clientWriter = java.io.PrintWriter(client.getOutputStream(), true)
                var clientMessage = clientReader.readLine().trim().uppercase()
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
                        if (portInput != null && commandPortScanner.isKillable(portInput, targetPid)) {
                            killThatProcessDude.killProcess(targetPid)
                        } else if (portInput == null) {
                            clientWriter.println("This port doesn't exist.")
                        } else if (!(commandPortScanner.isKillable(portInput, targetPid))) {
                            clientWriter.println("This process is not killable.")
                        } else {
                            clientWriter.println("An error occurred while checking if the port: $portInput is killable. try again.")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("You've written a wrong command.")
        }

    }

}