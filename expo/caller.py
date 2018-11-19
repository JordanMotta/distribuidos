import xmlrpc.client

class Caller:
    def __init__ (self):
        self.server = xmlrpc.client.ServerProxy ('http://localhost:6666')
        self.methods = {
            0 : self.add,
            1 : self.dec,
            2 : self.mul,
            3 : self.div,
            4 : self.factorial,
            5 : self.fibonacci,
            6 : self.foo
        }

    def add (self, x, y):
        return self.server.add (x, y)

    def dec (self, x, y):
        return self.server.dec (x, y)

    def mul (self, x, y):
        return self.server.mul (x, y)

    def div (self, x, y):
        return self.server.div (x, y)

    def factorial (self, n):
        return self.server.factorial (n)

    def fibonacci (self, n):
        return self.server.fibonacci (n)
    
    def foo (self):
        return self.server.foo ()
