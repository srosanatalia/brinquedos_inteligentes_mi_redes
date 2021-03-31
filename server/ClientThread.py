import socket, threading

class ClientThread(threading.Thread):

    def __init__(self,ip,port,clientsocket):
        threading.Thread.__init__(self)
        self.ip = ip
        self.port = port
        self.clientsock = clientsocket
        print("[+] Nova conexão iniciada por "+ip+":"+str(port))

    def run(self):    
        data = '.'
        while True and len(data):
            data = self.clientsock.recv(2048).decode()
            print("Cliente (%s:%s) enviou: %s"%(self.ip, str(self.port), data))

        print("Cliente "+self.ip+":"+str(self.port)+" desconectado...")