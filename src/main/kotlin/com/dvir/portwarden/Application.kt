package com.dvir.portwarden

import com.dvir.portwarden.model.PortInfo

import com.dvir.portwarden.model.Commands

import com.dvir.portwarden.service.PortScanner

import com.dvir.portwarden.service.ProcessManager

import com.dvir.portwarden.service.TcpServer

fun main(){

    val scanner = PortScanner()
    val manager = ProcessManager()
    val server = TcpServer()
    println("starting server on port 8080...")
    server.startServer(8080)
    val ports = scanner.scanListeningPorts()

    ports.forEach { portInfo -> 
        
        println("Port: ${portInfo.port} | PID: ${portInfo.pid} ")

    }

}