# Request/Reply pattern with Spring Integration

This is a sample boot application using Spring integration to demonstrate a
typical request/reply scenario using JMS

A big thanks to Gary Russell who provided the code sample and the explanations
below.

## Running

To run the example, simple invoke the `main` method of `JmsRequestReply` which
should give you some output about what the sample is doing. A simple embedded
JMS broker is started as part of the sample so no external dependency is 
required.

If everything works as it should, you should get something like this

```
--------- Sending foo and waiting for reply --------
FOO
----------------------------------------------------
```

indicating that the reply has been received synchronously. 

## Customizing

### Temporary queue

If the request/reply needs to be handled privately to the request, simply 
commented out the following line

```
gateway.setReplyDestinationName("test.in");
```

If not reply destination is set, a temporary queue is created automatically.

### Receive timeout

It is possible to customize the maximum amount of time the receiver waits for
the reply. First, the value of the maximum value should be set on the gateway

```
gateway.setReceiveTimeout(2000L); // Timeout after 2sec
gateway.setRequiresReply(true); // Throws an exception if no reply received
```

If no reply is received within 2sec, the JMS client will return `null` internally
so we have to tell the gateway not to wait any further.

The way spring integration works (with gateways) is the final message consumer 
(in this case the JMS gateway) sends the reply to an internal reply channel. When
the sending thread returns, it will try to receive the reply from that channel and,
by default, wait indefinitely. So if the JMS gateway doesn't return a reply, we must
set the timeout to 0 so we won't wait for a reply that will never come. 


```
@MessagingGateway(defaultRequestChannel = "requests", defaultReplyTimeout=0)
```

