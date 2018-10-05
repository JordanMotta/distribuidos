import xmlrpc.client

if __name__ == "__main__":
    server_url = "localhost/rpc-expo/math.php"
    # server = xmlrpc.client.ServerProxy (server_url)
    server = xmlrpc.client.ServerProxy (server_url)

    result = server.foo ()
    print (result)