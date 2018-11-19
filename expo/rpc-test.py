import xmlrpc.client

s = xmlrpc.client.ServerProxy ('http://localhost:6666')

print (s.add (4, 7))