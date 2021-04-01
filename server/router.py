import json

from SensorThread import SensorThread

def routes(request, body):
    method, url = request.split(' ')

    if method == 'POST' and url == '/rfid/config':
        post_rfid_config(body)

def post_rfid_config(body):
    data = json.loads(body)
    with open('configs/rfid.json', 'w') as file:
        json.dump(data, file, indent=2)

    sensor = SensorThread(
        data['serial'], 
        data['baudrate'], 
        data['region'], 
        data['protocol'], 
        data['antenna'], 
        data['frequency']
    )
    sensor.run()