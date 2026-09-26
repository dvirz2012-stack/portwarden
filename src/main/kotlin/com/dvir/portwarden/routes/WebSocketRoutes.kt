package com.dvir.portwarden.routes

import com.dvir.portwarden.service.PortScanner

import com.dvir.portwarden.service.ProcessManager

import com.dvir.portwarden.model.Commands

import com.dvir.portwarden.model.JsonResponse

import com.dvir.portwarden.model.Types

import org.java_websocket.server.WebSocketServer

import org.java_websocket.WebSocket

import org.java_websocket.handshake.ClientHandshake

import com.google.gson.Gson

import java.net.InetSocketAddress

import java.lang.Exception

class WebSocketRouter(port: Int): WebSocketServer(InetSocketAddress(System.getenv("WS_HOST") ?: "127.0.0.1", port)){

    val webSocketPortScanner = PortScanner()
    val theOneWhoKillsButInWebSocket = ProcessManager()
    val waitingForPort = mutableSetOf<WebSocket>()
    val ourGson = Gson()

    override fun onStart(){

        println("Websocket server started on port $port")

    }

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake? ){

        println("Web client connected: ${conn.remoteSocketAddress}")

    }

    override fun onMessage(conn: WebSocket, message: String){

        println("Client says: $message")

        if (conn in waitingForPort){

            waitingForPort.remove(conn)

            val portWritten: Int? = message.trim().toIntOrNull()
            var targetPid = -1L

            val currPorts = webSocketPortScanner.scanListeningPorts()

            currPorts.forEach { portInfo ->

                if (portInfo.port == portWritten) {
                    targetPid = portInfo.pid
                }

            }

            val response = when {

                portWritten == null || targetPid == -1L -> JsonResponse(

                    message = "No process listening on that port",
                    data = portWritten ?: -1,
                    type = Types.ERROR

                )

                !webSocketPortScanner.isKillable(portWritten, targetPid) -> JsonResponse(

                    message = "Port owner changed, try again",
                    data = targetPid,
                    type = Types.ERROR

                )

                !theOneWhoKillsButInWebSocket.killProcess(targetPid) -> JsonResponse(

                    message = "Couldn't kill process (permission denied?)",
                    data = targetPid,
                    type = Types.ERROR

                )

                else -> JsonResponse(

                    message = "Process killed successfully",
                    data = targetPid,
                    type = Types.SUCCESS

                )

            }

            conn.send(ourGson.toJson(response))

        } else {

            try {



                val command = Commands.valueOf(message.trim().uppercase())

                when (command) {

                    Commands.FETCH_PORTS -> {

                        val currPorts = webSocketPortScanner.scanListeningPorts()

                        val jsonResponseForPortsFetched = JsonResponse(


                            message = "Ports fetched successfully",
                            data = currPorts,
                            type = Types.SUCCESS

                        )

                        conn.send(ourGson.toJson(jsonResponseForPortsFetched))

                    }

                    Commands.KILL_PROCESS -> {

                        val jsonResponseForAskingForAPort = JsonResponse(

                            message = "Enter Port to Kill: ",
                            data = "",
                            type = Types.SUCCESS

                        )

                        conn.send(ourGson.toJson(jsonResponseForAskingForAPort))

                        waitingForPort.add(conn)

                    }

                }

            } catch (e: Exception){

                val jsonResponseForUnknownCommand = JsonResponse(

                    message = "Unknown command",
                    data = -1,
                    type = Types.ERROR

                )

                conn.send(ourGson.toJson(jsonResponseForUnknownCommand))

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

