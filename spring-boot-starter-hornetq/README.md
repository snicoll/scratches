# Spring Boot Starter HornetQ

This Spring Boot starter integrates with the HornetQ JMS broker. It provides an automatic
configuration of the `javax.jms.ConnectionFactory` when the HornetQ APIs are present.

The starter can be used with the following modes, defined by the `spring.hornetq.mode`
property:

1. `native` creates a Netty connection to the HornetQ broker available at `localhost` on
   the default port (i.e. `5445`)
2. `embedded` creates an embedded HornetQ broker

## Native mode

The native mode is the default and is used to connect to a HornetQ broker available on the
local machine with the default settings. It is possible to customize the location easily
by specifying the `spring.hornetq.host` and `spring.hornetq.port` properties.

## Embedded mode

The embedded mode uses a non persistent disk store by default and is mostly useful for
testing purposes. HornetQ does not allow to create a destination _on-the-fly_, i.e.
create the destination the first time it is requested.

It is possible however to create the necessary destinations on startup as follows:

* Set `spring.hornetq.embedded.queues` and spring.hornetq.embedded.topics` with the
  comma separated list of queues and topics to create on startup. These destinations
  are created with the default settings
* Any bean of type `JMSQueueConfiguration` or `TopicConfiguration` is used to create
  the corresponding destination. This allows you to fine-tune the configuration. Check
  these HornetQ-specific classes for more details.

When running in embedded mode, the following beans are exposed:

* General broker configuration: `org.hornetq.core.config.Configuration`
* JMS-specific configuration: `org.hornetq.jms.server.config.JMSConfiguration`
* The embedded broker itself: `org.hornetq.jms.server.embedded.EmbeddedJMS`

You can define any of those beans in your own configuration if you want to replace
the default behavior. For instance, if you want a full control over the way destinations
are registered, you can just define a bean of type `JMSConfiguration` and it will
take precedence. Check `HornetQEmbeddedConfiguration` for more details.
