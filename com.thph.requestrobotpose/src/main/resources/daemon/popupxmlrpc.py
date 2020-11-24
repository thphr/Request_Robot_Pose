import xmlrpclib
from SimpleXMLRPCServer import SimpleXMLRPCServer

def showPopup(request):
    if(request == True):
        print "Show popup"

def cancelPopup(request):
    if(request == True):
        print "Cancel popup"

server = SimpleXMLRPCServer(("", 33000), allow_none=True)
print "Listening on port 33000..."

server.register_function(showPopup,"showPopup")
server.register_function(cancelPopup,"cancelPopup")

server.serve_forever()