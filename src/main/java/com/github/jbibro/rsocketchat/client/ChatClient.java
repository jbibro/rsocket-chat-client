package com.github.jbibro.rsocketchat.client;

import java.io.IOException;
import java.util.stream.Stream;

import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ChatClient {

    public static void main(String[] args) throws IOException {
        Console console = new Console();
        String user = console.readLine("who are you?");
        RSocketFactory
            .connect()
            .transport(TcpClientTransport.create(8081))
            .start()
            .flatMapMany(rSocket -> rSocket
                .requestStream(DefaultPayload.create(user))
                .map(Payload::getDataUtf8)
                .doOnNext(it -> console.printLine("unknown", it))
                .zipWith(
                    Flux
                        .fromStream(Stream.generate(() -> console.readLine(">")))
                        .flatMap(x -> rSocket.fireAndForget(DefaultPayload.create(x)))
                        .subscribeOn(Schedulers.elastic())
                )
            )
            .blockLast();
    }


}
