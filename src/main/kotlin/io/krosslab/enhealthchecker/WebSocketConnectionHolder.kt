package io.krosslab.enhealthchecker

import org.java_websocket.enums.ReadyState
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock

@Component
class WebSocketConnectionHolder(@Value("\${websocket.uri}") val webUri: String) {
    private val logger = LoggerFactory.getLogger(javaClass)
    enum class Status {
        Unknown,
        Alive,
        Dead
    }

    private val lock: ReentrantLock = ReentrantLock()
    private var status: AtomicReference<Status> = AtomicReference<Status>(Status.Unknown)

    @Scheduled(fixedDelay = 5000)
    fun updateStatus() {
        if (!lock.tryLock()) {
            return
        }
        val beforeStatus = status.get()
        val afterStatus: Status
        var client: WebSocketClientImpl? = null
        try {
            client = WebSocketClientImpl(webUri)
            client.connectBlocking()
            when (client.getReadyState()) {
                ReadyState.OPEN -> status.set(Status.Alive)
                ReadyState.CLOSED -> status.set(Status.Dead)
                else -> status.set(Status.Unknown)
            }
            afterStatus = status.get()

            if (beforeStatus != afterStatus) {
                logger.info("update status ${beforeStatus} -> ${afterStatus}")
            }
        } finally {
            try {
                lock.unlock()
            } catch(e: Throwable) {
                e.printStackTrace()
            }
            client?.close(1000)
        }
    }

    fun isAlive(): Boolean {
        return when(status.get()) {
            Status.Unknown -> {
                updateStatus()
                status.get() == Status.Alive
            }
            Status.Alive -> true
            Status.Dead -> false
        }
    }
}