/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author natalia
 */
public class Publisher {
    public MqttClient clienteMqtt;
    public String urlBroker;
    public String topic;
    public String mensagem;
    
    public Publisher(String url, String topic, String mensagem) throws MqttException {
        this.topic = topic;
        this.mensagem = mensagem;
        this.clienteMqtt = new MqttClient(this.urlBroker, "ClientePublisher");
        this.clienteMqtt.connect();
        
        MqttMessage messageSend = new MqttMessage((mensagem).getBytes());
        System.out.println("a");
        clienteMqtt.publish(topic, messageSend);
    }
    
}
