package com.dvir.portwarden.service

import com.dvir.portwarden.model.PortInfo

class PortScanner {

    fun scanListeningPorts(): List<PortInfo> {

        val results = mutableListOf<PortInfo>()

        val pidToNameMap = mutableMapOf<Long, String>()
        val taskProcess = ProcessBuilder("cmd.exe", "/c", "tasklist /FO CSV /NH").start()

        taskProcess.inputStream.bufferedReader().useLines { lines ->

            lines.forEach { line ->

                val parts = line.replace("\"","").split(",")
                if (parts.size >= 2){

                    val name = parts[0]
                    val currentPid = parts[1].toLongOrNull()
                    if (currentPid != null){

                        pidToNameMap[currentPid] = name

                    }

                }

            }


        }
        
        val process = ProcessBuilder("cmd.exe", "/c", "netstat -ano").start()
        process.inputStream.bufferedReader().useLines {lines -> 

            lines.filter { it.contains("LISTENING") }.forEach {line ->

                val newLine : String = line.trim()
                val newLineNoWhitespaces : List<String> = newLine.split("\\s+".toRegex())

                val addressString = newLineNoWhitespaces[1]
                var pid = newLineNoWhitespaces[4].toLong()
                var port = addressString.substringAfterLast(":").toInt()

                val processName = pidToNameMap[pid] ?: "Unknown"
                results.add(PortInfo(port, pid, processName))

            }
        }
        return results.distinct()
    }

    fun isKillable(targetPort: Int, pid: Long): Boolean {
        
        return scanListeningPorts().any {
            it.port == targetPort && it.pid == pid
        }

    }
}