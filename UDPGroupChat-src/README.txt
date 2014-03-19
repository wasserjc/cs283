README for UDP Server: CS 283 Assignment 3

Please test by using (nc -u localhost 20000) netcat.
Here are the commands that can be used to interact with the server:
REGISTER <ID#> <Name>
UNREGISTER <ID#>
SEND <GroupName> <ID#> <Message>
JOIN <GroupName> <ID#>
POLL <ID#>
ACK <ID#>
SHUTDOWN

I have also implemented the following extra credit functionality:
LISTGROUPS <ID#>
QUIT <GroupName> <ID#>

You must register a client before joining groups, sending messages, etc.

When you poll, you will have ten seconds to respond using "ACK <ID#>" to acknowledge that you received the message. Otherwise the server will send the message again. It will require an ACK for each message currently in its message queue while you poll.

LISTGROUPS will list all groups that the client using <ID#> is currently a part of.
QUIT <GroupName> <ID#> will remove the client corresponding to that ID from the group corresponding to GroupName.

Example Functionality (copied from my terminal window):
jaredwasserman$ nc -u localhost 20000
REGISTER 123 Jared
Server: REGISTERED Jared
JOIN ExampleGroup 123
Server: JOINED
SEND ExampleGroup 123 This is a test.
Server: SENT ExampleGroup This is a test.
POLL 123
(ExampleGroup) Jared:  This is a test.
ACK 123
Server: Successfully POLLED
QUIT ExampleGroup 123
Server: QUIT GROUP ExampleGroup
UNREGISTER 123
Server: UNREGISTERED
SHUTDOWN
Server: SHUTDOWN REQUEST RECEIVED
