package com.dvir.portwarden.routes

import com.dvir.portwarden.service.PortScanner

import com.dvir.portwarden.service.ProcessManager

import com.dvir.portwarden.model.Commands

import org.java_websocket.server.WebSocketServer

import org.java_websocket.WebSocket

import org.java_websocket.handshake.ClientHandshake

import java.net.InetSocketAddress

import java.lang.Exception

class WebSocketRouter(port: Int): WebSocketServer(InetSocketAddress("127.0.0.1", port)){

    val webSocketPortScanner = PortScanner()
    val theOneWhoKillsButInWebSocket = ProcessManager()
    val waitingForPort = mutableSetOf<WebSocket>()

    override fun onStart(){

        println("Websocket server started on port $port")

    }

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake? ){

        println("Web client connected: ${conn.remoteSocketAddress}")

    }

    override fun onMessage(conn: WebSocket, message: String){

        println("Client says: $message")

        if (conn in waitingForPort){

            var portWritten: Int? = message.trim().toIntOrNull()
            var targetPid = -1L

            val currPorts = webSocketPortScanner.scanListeningPorts()

            currPorts.forEach { portInfo ->

                if (portInfo.port == portWritten) {
                    targetPid = portInfo.pid
                }

            }

            if (portWritten != null && webSocketPortScanner.isKillable(portWritten, targetPid)){

                theOneWhoKillsButInWebSocket.killProcess(targetPid)
                conn.send("Process $targetPid killed.")

            } else {

                conn.send("Cannot kill port $portWritten.")

            }

            waitingForPort.remove(conn)

        } else {

            try {



                val command = Commands.valueOf(message.trim().uppercase())

                when (command) {

                    Commands.FETCH_PORTS -> {

                        val currPorts = webSocketPortScanner.scanListeningPorts()
                        conn.send(currPorts.joinToString("\n"))

                    }

                    Commands.KILL_PROCESS -> {

                        conn.send("Enter the port the process Lives in: ")
                        waitingForPort.add(conn)

                    }

                }

            } catch (e: Exception){

                conn.send("Invalid command.")

            }

        }

    }

    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {

        println("Client left the room, reason: $reason")
        waitingForPort.remove(conn)

    }

    override fun onError(conn: WebSocket, ex: Exception){

        println("Websocket error: ${ex.message}")

    }

}

