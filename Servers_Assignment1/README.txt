Jared Wasserman
Computer Networks
Assignment 1

Design of BenchmarkClient :
The Benchmark Client is designed to very rapidly attempt to create 10,000
different connections with the server, where it sends a letter and expects
that letter back in capital form. I compare the time before all the requests
are completed to the time after all requests have been serviced, and display
that total time at the end of the Benchmark Client execution.

Sample Output:
** This first run sends the requests to the MTServer.
$ java BenchmarkClient
Average Time per request (ms): 2112

** This second run sends the requests to the SimpleServer.
Jared-Wassermans-MacBook-Pro:Servers_Assignment1 jaredwasserman$ java BenchmarkClient
Average Time per request (ms): 14764

The multithreaded server performs better because we are able to run service
operations in a background thread, which means that we do not have to wait on the server
side to complete one request before we start service on another request. Allowing us
to simultaneously service multiple requests from the Benchmark Client in various background
threads makes the multithreaded server perform much better, especially as the number
of incoming requests increases.
