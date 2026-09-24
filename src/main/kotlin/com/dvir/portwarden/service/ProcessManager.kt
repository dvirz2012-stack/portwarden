package com.dvir.portwarden.service

class ProcessManager {

    fun killProcess(pid: Long){

            ProcessHandle.of(pid).ifPresent{it.destroyForcibly()}

    }

}