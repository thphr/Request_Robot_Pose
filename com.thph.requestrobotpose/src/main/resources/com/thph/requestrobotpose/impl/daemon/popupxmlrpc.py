import xmlrpclib
from SimpleXMLRPCServer import SimpleXMLRPCServer

isShowing = False

def showpopup():
    isShowing = True
    return isShowing

def isEnabled():
    return isShowing

def cancelpopup():
    isShowing = False
    return isShowing

server = SimpleXMLRPCServer(("", 40405), allow_none=True)

print "Listening on port 40405..."

server.register_function(showpopup,"showpopup")
server.register_function(cancelpopup,"cancelpopup")
server.register_function(isEnabled,"isEnabled")

server.serve_forever()
