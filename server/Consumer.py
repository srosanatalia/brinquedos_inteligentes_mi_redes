'''
Classe resposável por gerar os resultados e enviar para o cliente.
Funciona em dois modos:
    - Qualificação
    - Corrida
'''

import threading
import time

class Consumer(threading.Thread):
    def __init__(self, buffer, race_config, clients, type):
        threading.Thread.__init__(self)
        self.buffer = buffer 
        self.clients = clients 
        self.type = type
        self.cars = {}

        for car in race_config['cars']:
            self.cars[bytes(car, "utf-8")] = {"laps": []}

    def run(self):
        self.__consume()

    def __consume(self):
        # Condicional para definir a mensagem de encerramento dependendo do modo
        if self.type == 'qualification':
            msg_finished = "QUALIFICATION_COMPLETED"
        elif self.type == 'race':
            msg_finished = "RACE_COMPLETED"

        # Laço para esperar o buffer ser preenchido
        while self.buffer.is_empty():
            continue

        # Pega primeiro dado do buffer que sempre será o tempo de inicio
        self.time_start = self.buffer.remove()

        data = ''
        while True:
            while self.buffer.is_empty():
                continue

            data = self.buffer.remove()
            # Verifica se se é a mensagem de finalização 
            if data != msg_finished:
                self.__update(data)
            else:
                break
        if self.type == 'qualification':
            print("QUALIFICATION COMPLETED")
        elif self.type == 'race':
            print("RACE COMPLETED")
        # Envia para todos clientes conectados a mensagem de finalização
        for client in self.clients:
            time.sleep(1)
            client.clientsock.sendall(f'{msg_finished}!'.encode("utf-8"))
    
    # Atualização das informações com base no modo, 
    # sendo que após atualização ele gera um resultado e envia para o cliente
    def __update(self, data):
        if self.type == 'qualification':
            self.__update_qualification(data)
        elif self.type == 'race':
            self.__update_race(data)

    def __update_qualification(self, data):
        if data['lap'] == 1:
            time = data['timestamp'] - self.time_start
        else:
            time = data['timestamp'] - self.cars[data['epc']]['laps'][data['lap']-2]['timestamp']

        self.cars[data['epc']]['laps'].insert((data['lap']-1), {'time': time, 'timestamp': data['timestamp']})

        result = self.__get_result_qualification()
        for client in self.clients:
            client.clientsock.sendall(f"OK\n{result}!".encode("utf-8"))

    def __get_result_qualification(self):
        result = []
        best_time_qualification = None  # Tempo da melhor volta
        for car in self.cars:
            result_car = {'epc': car, 'best_time': None}
            for lap in self.cars[car]['laps']:
                # Verifica se o tempo da volta é o melhor do carro
                if (not result_car['best_time']) or (lap['time'] < result_car['best_time']):
                    result_car['best_time'] = lap['time']
                    if (not best_time_qualification) or (lap['time'] < best_time_qualification):
                        best_time_qualification = lap['time']
            if result_car['best_time'] != None:
                result.append(result_car)
        
        # Ordena os resultados e trata eles para envio e exibição do cliente
        result_order = sorted(result, key=lambda k: k['best_time'])
        for car in result_order:
            if car['best_time'] == best_time_qualification:
                car['time'] = str(car['best_time'])[2:]
            else:
                car['time'] = car['best_time'] - best_time_qualification
                car['time'] = '+'+str(car['time'])[2:]
            car['best_time'] = str(car['best_time'])[2:]
        return result_order

    def __update_race(self, data):
        if data['lap'] == 1:
            time = data['timestamp'] - self.time_start
        else:
            time = data['timestamp'] - self.cars[data['epc']]['laps'][data['lap']-2]['timestamp']

        self.cars[data['epc']]['laps'].insert((data['lap']-1), {'time': time, 'timestamp': data['timestamp']})

        result = self.__get_result_race()
        for client in self.clients:
            client.clientsock.sendall(f"OK\n{result}!".encode("utf-8"))

    def __get_result_race(self):
        result = []
        best_time_race = None
        for car in self.cars:
            result_car = {'epc': car, 'race_time': None, 'best_time': None, 'time_lap': None, 'laps': len(self.cars[car]['laps'])}
            for lap in self.cars[car]['laps']:
                result_car['time_lap'] = lap['time']
                # Soma o tempo da volta com o tempo que o carro já levou na corrida
                if not result_car['race_time']:
                    result_car['race_time'] = lap['time']
                else:
                    result_car['race_time'] += lap['time']

                if (not result_car['best_time']) or (lap['time'] < result_car['best_time']):
                    result_car['best_time'] = lap['time']
            if result_car['best_time'] != None:
                result.append(result_car)
        
        # Ordena os resultados e trata eles para envio e exibição do cliente
        result_order = multisort(list(result), (('laps', True), ('race_time', False)))
        for car in result_order:
            if (not best_time_race):
                best_time_race = car['race_time']
                car['race_time'] = str(car['race_time'])[2:]
                laps_best_time_race = car['laps']
            else:
                if car['laps'] == laps_best_time_race:
                    car['race_time'] = car['race_time'] - best_time_race
                    car['race_time'] = '+'+str(car['race_time'])[2:]
                else:
                    car['race_time'] = f"+{laps_best_time_race - car['laps']} voltas"
            car['time_lap'] = str(car['time_lap'])[2:]
            car['best_time'] = str(car['best_time'])[2:]
        return result_order
        
def multisort(xs, specs):
    for key, reverse in reversed(specs):
        xs.sort(key=lambda k: key, reverse=reverse)
    return xs