'''
Classe resposável por manipular a leitura e o tratamento dos dados vindo do módulo RFID.
Funciona em dois modos:
    - Qualificação
    - Corrida

Nos dois casos o produtor manipula o tempo de leitura com base no tempo minimo de volta,
isto é, o leitor não irá ler num intervalo de tempo impossível de haver leituras.
Além de que é determinado um tempo máximo de qualificação/corrida,
para caso algum carro não termine nem a primeira volta ou a corrida toda
'''

from datetime import datetime, timedelta
import threading
import sys

class Producer(threading.Thread):
    def __init__(self, buffer, sensor, race_config, type):
        threading.Thread.__init__(self)
        self.buffer = buffer 
        self.type = type 
        self.sensor = sensor
        self.min_lap_time = int(race_config['min_time_speedway'])
        self.cars = {}
        self.num_cars = 0
        self.current_lap = 0
        self.time_next_read = None
        self.num_laps = int(race_config['num_laps_race'])
        self.max_time = race_config['max_time_qualification']
        self.cars_ended_lap = []
        self.cars_ended_race = []

        for car in race_config['cars']:
            self.cars[bytes(car, "utf-8")] = {"current_lap": 0, "first_read_lap": None, "next_start_read": None}
            self.num_cars += 1
            self.max_time_end = int(race_config['max_time_qualification'])

    def run(self):
        if self.type == 'qualification':
            self.__start_qualification()
        if self.type == 'race':
            self.__start_race()
    
    def __start_qualification(self):
        self.time_start = datetime.now()    # Tempo de inicio da qualificação
        self.time_end = self.time_start + timedelta(seconds=self.max_time_end)  # Tempo para finalizar qualificação
        self.time_next_read = self.time_start + timedelta(seconds=self.min_lap_time)    # Tempo minimo para uma próxima leitura
        self.buffer.add(self.time_start)
        for car in self.cars:
            self.cars[car]['next_start_read'] = self.time_start + timedelta(seconds=self.min_lap_time) # Tempo minimo para uma próxima leitura o carro
            self.cars[car]['first_read_lap'] = self.time_start # Tempo da primeira leitura da volta
        timeout = self.time_start + timedelta(seconds=self.max_time_end+self.min_lap_time) # Tempo limite para qualificação
        while datetime.now() <= self.time_end: # Laço para qualificação ficar rodando enquanto estiver no tempo
            # Laço para esperar até tempo minimo de próxima leitura para começar a ler
            while datetime.now() < self.time_next_read:
                continue
            self.sensor.read_data(self.treat_data)
            # Laço para esperar até que todos carros terminem a volta que iniciou ou até o limite máximo de qualificação
            while len(self.cars_ended_lap) != self.num_cars and datetime.now() < timeout:
                continue
            self.sensor.stop_read_data()
            self.cars_ended_lap = []
        self.buffer.add("QUALIFICATION_COMPLETED")
        print("QUALIFICATION_COMPLETED_PRODUCER")
    
    def __start_race(self):
        self.time_start = datetime.now()
        self.time_next_read = self.time_start + timedelta(seconds=self.min_lap_time)
        self.buffer.add(self.time_start)
        for car in self.cars:
            self.cars[car]['next_start_read'] = self.time_start + timedelta(seconds=self.min_lap_time)
            self.cars[car]['first_read_lap'] = self.time_start
        timeout = self.time_start + timedelta(seconds=self.min_lap_time*(self.num_laps+2))
        while len(self.cars_ended_race) != self.num_cars and datetime.now() < timeout:
            while datetime.now() < self.time_next_read:
                continue
            self.sensor.read_data(self.treat_data)
            # Laço para esperar até que todos carros terminem a volta que iniciou ou até todos completarem a corrida
            #  ou até o limite máximo de qualificação
            while len(self.cars_ended_lap) != self.num_cars and datetime.now() < timeout and len(self.cars_ended_race) != self.num_cars:
                continue
            self.sensor.stop_read_data()
            self.cars_ended_lap = []
        self.buffer.add("RACE_COMPLETED")
        print("RACE_COMPLETED_PRODUCER")

    def treat_data(self, epc, rssi, timestamp):
        try:
            # Verificação se a tag lida está na corrida
            if epc in self.cars.keys() and (type == 'qualification' or epc not in self.cars_ended_race):
                # Verifica se a tag foi lida após o tempo minimo de volta 
                if timestamp >= self.cars[epc]['next_start_read']:
                    self.cars[epc]['current_lap'] += 1
                    # Define tempo minimo da próxima leitura
                    self.cars[epc]['next_start_read'] = timestamp + timedelta(seconds=self.min_lap_time)
                    # Salva informações no buffer
                    self.buffer.add({"epc": epc, "timestamp": timestamp, "lap": self.cars[epc]['current_lap']})
                    if self.cars[epc]['current_lap'] > self.current_lap: # Verificação para atualizar a volta atual da corrida
                        self.current_lap = self.cars[epc]['current_lap']
                        self.time_next_read = self.cars[epc]['next_start_read']
                else:
                    if epc not in self.cars_ended_lap:
                        self.cars_ended_lap.append(epc)
                    # Verifica se carro já fez todas voltas da corrida
                    if self.type == 'race' and epc not in self.cars_ended_race and self.cars[epc]['current_lap'] == self.num_laps:
                        self.cars_ended_race.append(epc)
        except Exception as e:
            self.sensor.stop_read_data()
            print(e)
            sys.exit(0)