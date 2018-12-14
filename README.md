# ClientServerApp
Simple Client - Server application 

The communication is made using sockets.
I created some HTTP requests from client side to server side.  
  
The server is looking into the Credentials file to see if 
the user has rights to access that resource.
The file stores base64 encodings of the
"username":"password".
This is created just as an example, it should be used with
a proper encryption algorithm.


The authentication is made using basic authentication.

In order to test the app the server must be open and
after that the tests from ClientServerSpec can be run. 
