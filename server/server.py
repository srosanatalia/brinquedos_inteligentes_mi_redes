import signal
import socket
import sys

from ClientThread import ClientThread
from SensorThread import SensorThread
from bcolors import bcolors

def end_execution_handler(sig, frame):
    sys.stdout.write('\b\b')
    response = input(f"{bcolors.YELLOW}Deseja mesmo encerrar o servidor? (Y/n) {bcolors.COLOR_OFF}")
    if response == 'Y' or response == 'y':
        print(f"{bcolors.BRED}Servidor finalizado...{bcolors.COLOR_OFF}")
        sys.exit(0)
    else:
        print(f"{bcolors.GREEN}Servidor continua em execução...{bcolors.COLOR_OFF}")


signal.signal(signal.SIGINT, end_execution_handler)

serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

clients = []

host = ''
port = int(input('Digite a porta desejada: '))
while True:
    try:
        serversocket.bind((host, port))
    except OSError:
        port = int(input('Porta já utilizada, por favor, digite outra: '))
    else:
        break
print(f"{bcolors.GREEN}Servidor iniciado em augusto.ddns.net:{port}...{bcolors.COLOR_OFF}")

while True:
    serversocket.listen(4)
    (clientsock, (ip, port)) = serversocket.accept()

    new_client = ClientThread(ip, port, clientsock)
    new_client.start()

    clients.append(clients) 
