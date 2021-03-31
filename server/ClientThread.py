import socket
import threading

import router
from bcolors import bcolors

class ClientThread(threading.Thread):

    def __init__(self, ip, port, clientsocket):
        threading.Thread.__init__(self)
        self.ip = ip
        self.port = port
        self.clientsock = clientsocket
        print(f"{bcolors.GREEN}[+]{bcolors.COLOR_OFF} Nova conexão iniciada por {ip}:{str(port)}")

    def run(self):    
        data = '.'
        while True and len(data):
            data = self.clientsock.recv(2048).decode()
            if len(data):
                request, body = data.split('\n')
                print(f"Cliente ({self.ip}:{self.port}) chamou a rota: {request}")
                router.routes(request, body)

        print(f"{bcolors.RED}[-]{bcolors.COLOR_OFF} Cliente {self.ip}:{str(self.port)} desconectado...")

