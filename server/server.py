import socket
from ClientThread import ClientThread

serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

clients = []

host = ''
port = int(input('Digite a porta desejada: '))

serversocket.bind((host, port))
print("Servidor iniciado em augusto.ddns.net:{}...".format(port))

while True:
    serversocket.listen(4)
    (clientsock, (ip, port)) = serversocket.accept()

    new_client = ClientThread(ip, port, clientsock)
    new_client.start()

    clients.append(clients) 
