import threading

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
        if self.type == 'qualification':
            msg_finished = "QUALIFICATION_COMPLETED"
        elif self.type == 'race':
            msg_finished = "RACE_COMPLETED"

        while self.buffer.is_empty():
            continue

        self.time_start = self.buffer.remove()

        data = ''
        while True:
            while self.buffer.is_empty():
                continue

            data = self.buffer.remove()
            if data != msg_finished:
                if len(self.cars[data['epc']]['laps']) == data['lap']:
                    lap_current_lap = self.cars[data['epc']]['laps'][data['lap']-1]
                    if data['rssi'] > lap_current_lap['rssi']:
                        self.__update(data, 'update')
                else:
                    self.__update(data, 'new')
            else:
                break
        if self.type == 'qualification':
            print("QUALIFICATION COMPLETED")
        elif self.type == 'race':
            print("RACE COMPLETED")
        for client in self.clients:
            client.clientsock.sendall(f'{msg_finished}!'.encode("utf-8"))
    
    def __update(self, data, type):
        if self.type == 'qualification':
            self.__update_qualification(data, type)
        elif self.type == 'race':
            self.__update_race(data, type)

    def __update_qualification(self, data, type):
        if data['lap'] == 1:
            time = data['timestamp'] - self.time_start
        else:
            time = data['timestamp'] - self.cars[data['epc']]['laps'][data['lap']-2]['timestamp']

        if type == 'update':
            self.cars[data['epc']]['laps'][data['lap']-1] = {'rssi': data['rssi'], 'time': time, 'timestamp': data['timestamp']}
        elif type == 'new':
            self.cars[data['epc']]['laps'].insert((data['lap']-1), {'rssi': data['rssi'], 'time': time, 'timestamp': data['timestamp']})

        result = self.__get_result_qualification()
        for client in self.clients:
            client.clientsock.sendall(f"OK\n{result}!".encode("utf-8"))

    def __get_result_qualification(self):
        result = []
        best_time_qualification = None
        for car in self.cars:
            result_car = {'epc': car, 'best_time': None}
            for lap in self.cars[car]['laps']:
                if (not result_car['best_time']) or (lap['time'] < result_car['best_time']):
                    result_car['best_time'] = lap['time']
                    if (not best_time_qualification) or (lap['time'] < best_time_qualification):
                        best_time_qualification = lap['time']
            if result_car['best_time'] != None:
                result.append(result_car)
        
        result_order = sorted(result, key=lambda k: k['best_time'])
        for car in result_order:
            if car['best_time'] == best_time_qualification:
                car['time'] = car['best_time']
            else:
                car['time'] = car['best_time'] - best_time_qualification
            car['best_time'] = str(car['best_time'])[2:]
            car['time'] = str(car['time'])[2:]
        return result_order

    def __update_race(self, data, type):
        if data['lap'] == 1:
            time = data['timestamp'] - self.time_start
        else:
            time = data['timestamp'] - self.cars[data['epc']]['laps'][data['lap']-2]['timestamp']

        if type == 'update':
            self.cars[data['epc']]['laps'][data['lap']-1] = {'rssi': data['rssi'], 'time': time, 'timestamp': data['timestamp']}
        elif type == 'new':
            self.cars[data['epc']]['laps'].insert((data['lap']-1), {'rssi': data['rssi'], 'time': time, 'timestamp': data['timestamp']})

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
                if not result_car['race_time']:
                    result_car['race_time'] = lap['time']
                else:
                    result_car['race_time'] += lap['time']

                if (not result_car['best_time']) or (lap['time'] < result_car['best_time']):
                    result_car['best_time'] = lap['time']
            if result_car['best_time'] != None:
                result.append(result_car)
        
        result_order = sorted(result, key=lambda k: (k['laps'], k['race_time']))
        for car in result_order:
            if (not best_time_race):
                best_time_race = car['race_time']
            else:
                car['race_time'] = car['race_time'] - best_time_race
            car['race_time'] = str(car['race_time'])[2:]
            car['time_lap'] = str(car['time_lap'])[2:]
            car['best_time'] = str(car['best_time'])[2:]
        return result_order