import xmlrpclib
from SimpleXMLRPCServer import SimpleXMLRPCServer

def showpopup():
    return "Show popup"

def cancelpopup():
    return "Cancel popup"

server = SimpleXMLRPCServer(("", 40405), allow_none=True)

print "Listening on port 40405..."

server.register_function(showpopup,"showpopup")
server.register_function(cancelPopup,"cancelpopup")

server.serve_forever()
