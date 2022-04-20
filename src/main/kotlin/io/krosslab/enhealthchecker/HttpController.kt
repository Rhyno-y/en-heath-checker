package io.krosslab.enhealthchecker

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping
class HttpController(val webSocketConnectionHolder: WebSocketConnectionHolder) {

    @GetMapping("/", consumes = [MediaType.ALL_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun http(): ResponseEntity<String> {
        return ResponseEntity.ok("success")
    }

    @GetMapping("/websocket")
    fun websocket(): ResponseEntity<String> {
        val alive = webSocketConnectionHolder.isAlive()
        return ResponseEntity.ok(alive.toString())
    }
}