package com.github.jbibro.rsocketchat.client;

import java.io.IOException;

import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import jline.console.ConsoleReader;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ChatClient {
    public static void main(String[] args) throws IOException {
        ConsoleReader console = new ConsoleReader();
        String author = console.readLine("Who are you? > ");
        ConsoleIterable consoleIterable = new ConsoleIterable(console, author + "> ");

        RSocketFactory
            .connect()
            .transport(TcpClientTransport.create(8080))
            .start()
            .flatMapMany(rSocket ->
                rSocket
                    .requestChannel(
                        Flux.concat(
                            Flux.just("joined"),
                            Flux.fromIterable(consoleIterable)
                        )
                            .map(msg -> String.join("-", author, msg))
                            .map(DefaultPayload::create)
                            .subscribeOn(Schedulers.elastic())
                    )
                    .map(Payload::getDataUtf8)
                    .filter(it -> !it.startsWith(author))
                    .doOnNext(it -> {
                        String[] msg = it.split("-");
                        ConsoleUtil.printLine(console, msg[0], msg[1]);
                    })
            )
            .blockLast();
    }
}
