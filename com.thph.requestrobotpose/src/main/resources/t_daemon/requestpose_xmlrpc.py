#!/usr/bin/env python

import sys
import socket
import struct
import time

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn

isShowing = False
LOCALHOST = "127.0.0.1"

zNegative = False
zPositive = False

yNegative = False
yPositive = False

xNegative = False
xPositive = False

def setDirectionEnabled(toolspeed,enabled):
  if toolspeed == "zNegative":
    global zNegative
    zNegative = True
  if toolspeed == "zPositive":
    global zPositive
    zPositive = True
  if toolspeed == "yNegative":
    global yNegative
    yNegative = True
  if toolspeed == "yPositive":
    global yPositive
    yPositive = True
  if toolspeed == "xNegative":
    global xNegative
    xNegative = True
  if toolspeed == "xPositive":
    global xPositive
    xPositive = True

def getDirectionEnabled(toolspeed):
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

