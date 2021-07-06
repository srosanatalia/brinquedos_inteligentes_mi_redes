'''
    Em Desuso com o Uso de MQTT
'''
import socket
import threading

from bcolors import bcolors

class ClientThread(threading.Thread):

    def __init__(self, ip, port, clientsocket, controller):
        threading.Thread.__init__(self)
        self.controller = controller
        self.ip = ip
        self.port = port
        self.clientsock = clientsocket
        print(f"{bcolors.GREEN}[+]{bcolors.COLOR_OFF} Nova conexão iniciada por {ip}:{str(port)}")

    def run(self):    
        data = '.'
        # Loop esperando mensagens do cliente até ele desconectar
        while True and len(data):
            try:
                data = self.clientsock.recv(2048).decode("utf-8")
            except ConnectionResetError:
                break
            if len(data):
                request, body = data.split('\n') # Separa a requisição e corpo da mensagem do cliente
                print(f"Cliente ({self.ip}:{self.port}) chamou a rota: {request}")
                self.controller.routes(self, request, body) # Chama controller para tratar a chamada do cliente

        print(f"{bcolors.RED}[-]{bcolors.COLOR_OFF} Cliente {self.ip}:{str(self.port)} desconectado...")
        self.controller.remove_client(self) # Quando desconectado, remove-o da lista clientes conectados

