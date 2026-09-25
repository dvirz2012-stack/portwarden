package com.dvir.portwarden.service

import com.dvir.portwarden.model.PortInfo

class PortScanner {

    fun scanListeningPorts(): List<PortInfo> {

        val results = mutableListOf<PortInfo>()
        
        val process = ProcessBuilder("cmd.exe", "/c", "netstat -ano -p tcp").start()
        process.inputStream.bufferedReader().useLines {lines -> 

            lines.filter { it.contains("LISTENING") }.forEach {line ->

                val newLine : String = line.trim()
                val newLineNoWhitespaces : List<String> = newLine.split("\\s+".toRegex())
                val addressString = newLineNoWhitespaces[1]
                var pid = newLineNoWhitespaces[4].toLong()
                var port = addressString.substringAfterLast(":").toInt()
                results.add(PortInfo(port, pid))

            }
        }
        return results
    }

    fun isKillable(targetPort: Int, pid: Long): Boolean {
        
        return scanListeningPorts().any {
            it.port == targetPort && it.pid == pid
        }

    }
}