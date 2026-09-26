package com.dvir.portwarden.service

import java.util.concurrent.TimeUnit

class ProcessManager {

    fun killProcess(pid: Long): Boolean{

            val handle = ProcessHandle.of(pid).orElse(null) ?: return false

            if (handle.pid() == ProcessHandle.current().pid()){

                return false

            }

            if (!handle.destroyForcibly()){

                return false

            }

            try {

                handle.onExit().get(3, TimeUnit.SECONDS)

            } catch (e: Exception) {

            }

            return !handle.isAlive

    }

}