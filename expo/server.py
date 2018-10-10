from xmlrpc.server import SimpleXMLRPCServer
from xmlrpc.server import SimpleXMLRPCRequestHandler

class RequestHandler (SimpleXMLRPCRequestHandler):
    rpc_paths = ('/RPC2',)


s = SimpleXMLRPCServer (('localhost', 6666), requestHandler=RequestHandler)

s.register_introspection_functions ()

def add_function (x, y):
    return x + y

def dec_function (x, y):
    return x - y

def mul_function (x, y):
    return x * y

def div_function (x, y):
    return x / y

def factorial_function (n):
    if n == 1:
        return 1
    else:
        return n * factorial_function (n - 1)

def fibonacci_function (n):
    if n == 0:
        return 0
    if n == 1 or n  == 2:
        return 1
    return fibonacci_function (n - 1) + fibonacci_function (n - 2)

def foo_function ():
    for i in range (99999999):
        pass
    return 0

s.register_function (add_function, 'add')
s.register_function (dec_function, 'dec')
s.register_function (mul_function, 'mul')
s.register_function (div_function, 'div')
s.register_function (factorial_function, 'factorial')
s.register_function (fibonacci_function, 'fibonacci')
s.register_function (foo_function, 'foo')

s.serve_forever ()

