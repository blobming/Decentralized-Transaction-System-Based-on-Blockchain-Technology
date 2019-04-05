import socket
import sys
import threading
import time
 
class client():
    def __init__(self):
        self.s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        self.ip = "127.0.0.1"
 
    def connect(self):
        try:
            self.s.connect((self.ip,8016))
            print("connect success")
            print('connect time: '+time.ctime())
        except ConnectionError:
            print('connect error')
            sys.exit(-1)
        except:
            print('unexpect error')
            sys.exit(-1)
 
    def send_sth(self):
        sth=input('> ')
        try:
            self.s.sendall((sth+"\r").encode())
        except ConnectionError:
            print('connect error')
            sys.exit(-1)
        except:
            print('unexpect error')
            sys.exit(-1)
 
    def receive(self):
        s = ""
        try:
            receivedMessage = self.s.recv(1024).decode('utf-8')
            s += receivedMessage
        except:
            print("")
        return s
 
c1 = client()
c1.connect()
while True:
    print(c1.receive())
    c1.send_sth()
