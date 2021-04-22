import threading

class Consumer(threading.Thread):
    def __init__(self, buffer, race_config, type):
        threading.Thread.__init__(self)
        self.buffer = buffer 
        self.type = type
        self.cars = {}

        if self.type == 'qualification':
            for car in race_config['cars']:
                self.cars[bytes(car, "utf-8")] = {"laps": []}

    def run(self):
        if self.type == 'qualification':
            self.__consume_qualification()

    def __consume_qualification(self):
        while self.buffer.is_empty():
            continue

        self.time_start = self.buffer.remove()

        data = ''
        while True:
            while self.buffer.is_empty():
                continue

            data = self.buffer.remove()
            if data != "QUALIFICATION_COMPLETED":
                if len(self.cars[data['epc']]['laps']) == data['lap']:
                    lap_current_lap = self.cars[data['epc']]['laps'][data['lap']-1]
                    if data['rssi'] > lap_current_lap['rssi']:
                        self.__update_qualification(data, 'update')
                else:
                    self.__update_qualification(data, 'new')
            else:
                break
        print("QUALIFICATION COMPLETED")

    def __update_qualification(self, data, type):
        if data['lap'] == 1:
            time = data['timestamp'] - self.time_start
        else:
            time = data['timestamp'] - self.cars[data['epc']]['laps'][data['lap']-2]['timestamp']

        if type == 'update':
            self.cars[data['epc']]['laps'][data['lap']-1] = {'rssi': data['rssi'], 'time': time, 'timestamp': data['timestamp']}
        elif type == 'new':
            self.cars[data['epc']]['laps'].insert((data['lap']-1), {'rssi': data['rssi'], 'time': time, 'timestamp': data['timestamp']})

        print(self.__get_result_qualification())

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