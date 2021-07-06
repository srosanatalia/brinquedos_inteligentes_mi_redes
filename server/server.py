from configs import path_default
import signal
import sys
from bcolors import bcolors
from ServerController import ServerController

# Função para encerramento do programa com ctrl+c
def end_execution_handler(sig, frame):
    sys.stdout.write('\b\b')
    response = input(f"{bcolors.YELLOW}Deseja mesmo encerrar o servidor? (Y/n) {bcolors.COLOR_OFF}")
    if response == 'Y' or response == 'y':
        print(f"{bcolors.BRED}Servidor finalizado...{bcolors.COLOR_OFF}")
        sys.exit(0)
    else:
        print(f"{bcolors.GREEN}Servidor continua em execução...{bcolors.COLOR_OFF}")

# Código para não exibir error caso seja digitado ctrl+c 
signal.signal(signal.SIGINT, end_execution_handler)

controller = ServerController()
while True:
    # Servidor fica rodando esperando novas mensagens no tópico autorama
    controller.check_new_messages()