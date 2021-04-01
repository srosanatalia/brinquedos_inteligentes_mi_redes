import signal
import socket
import sys

from bcolors import bcolors
from ServerController import ServerController

def end_execution_handler(sig, frame):
    sys.stdout.write('\b\b')
    response = input(f"\n{bcolors.YELLOW}Deseja mesmo encerrar o servidor? (Y/n) {bcolors.COLOR_OFF}")
    if response == 'Y' or response == 'y':
        print(f"{bcolors.BRED}Servidor finalizado...{bcolors.COLOR_OFF}")
        sys.exit(0)
    else:
        print(f"{bcolors.GREEN}Servidor continua em execução...{bcolors.COLOR_OFF}")


signal.signal(signal.SIGINT, end_execution_handler)

serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

host = ''
port = int(input(f"{bcolors.YELLOW}Digite a porta desejada: {bcolors.COLOR_OFF}"))
while True:
    try:
        serversocket.bind((host, port))
    except OSError:
        port = int(input(f"{bcolors.YELLOW}Porta já utilizada, por favor, digite outra: {bcolors.COLOR_OFF}"))
    else:
        break
controller = ServerController()
print(f"{bcolors.GREEN}Servidor iniciado em augusto.ddns.net:{port}...{bcolors.COLOR_OFF}")


while True:
    serversocket.listen(4)
    (clientsock, (ip, port)) = serversocket.accept()

    controller.add_client(ip, port, clientsock)
