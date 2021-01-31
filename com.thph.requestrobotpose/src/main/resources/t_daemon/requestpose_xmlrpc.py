#!/usr/bin/env python

import sys
import socket
import struct
import time

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn

isShowing = False
LOCALHOST = "127.0.0.1"

#TPC position
zNegative = False
zPositive = False

yNegative = False
yPositive = False

xNegative = False
xPositive = False

#TPC orientation
rzNegative = False
rzPositive = False

ryNegative = False
ryPositive = False

rxNegative = False
rxPositive = False

def setDirectionEnabled(toolspeed,enabled):
#TPC position
  if toolspeed == "zNegative":
    global zNegative
    zNegative = enabled
  if toolspeed == "zPositive":
    global zPositive
    zPositive = enabled
  if toolspeed == "yNegative":
    global yNegative
    yNegative = enabled
  if toolspeed == "yPositive":
    global yPositive
    yPositive = enabled
  if toolspeed == "xNegative":
    global xNegative
    xNegative = enabled
  if toolspeed == "xPositive":
    global xPositive
    xPositive = enabled
  if toolspeed == "rzNegative":
    global rzNegative
    zNegative = enabled
#TPC orientation
  if toolspeed == "rzPositive":
    global rzPositive
    rzPositive = enabled
  if toolspeed == "ryNegative":
    global ryNegative
    ryNegative = enabled
  if toolspeed == "ryPositive":
    global ryPositive
    ryPositive = enabled
  if toolspeed == "rxNegative":
    global rxNegative
    rxNegative = enabled
  if toolspeed == "rxPositive":
    global rxPositive
    rxPositive = enabled
  return True

def getDirectionEnabled(toolspeed):
#TPC position
  if toolspeed == "zNegative":
    global zNegative
    return zNegative
  if toolspeed == "zPositive":
    global zPositive
    return zPositive
  if toolspeed == "yNegative":
    global yNegative
    return yNegative
  if toolspeed == "yPositive":
    global yPositive
    return yPositive
  if toolspeed == "xNegative":
    global xNegative
    return xNegative
  if toolspeed == "xPositive":
    global xPositive
    return xPositive
#TPC orientation
  if toolspeed == "rzNegative":
    global rzNegative
    return rzNegative
  if toolspeed == "rzPositive":
    global rzPositive
    return rzPositive
  if toolspeed == "ryNegative":
    global ryNegative
    return ryNegative
  if toolspeed == "ryPositive":
    global ryPositive
    return ryPositive
  if toolspeed == "rxNegative":
    global rxNegative
    return rxNegative
  if toolspeed == "rxPositive":
    global rxPositive
    return rxPositive

def showpopup():
  global isShowing
  isShowing = True
  return isShowing

def isEnabled():
  global isShowing
  return isShowing

def cancelpopup():
  global isShowing
  isShowing = False
  return isShowing



class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
    pass


# Connection related functions
server = MultithreadedSimpleXMLRPCServer((LOCALHOST, 40405))
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
print "Listening on port 40405..."

server.register_function(showpopup,"showpopup")
server.register_function(cancelpopup,"cancelpopup")
server.register_function(isEnabled,"isEnabled")
server.register_function(setDirectionEnabled,"setDirectionEnabled")
server.register_function(getDirectionEnabled,"getDirectionEnabled")


server.serve_forever()

