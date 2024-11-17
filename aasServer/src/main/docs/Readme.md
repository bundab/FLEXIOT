# IotRegistrationSystemRepository

## Table of Contents
* **[1. Introduction](#1-introduction)**
* **[2. Pre-requirements](#2-pre-requirements)**
  * [2.1. Ubuntu](#21-ubuntu)
* **[3. Installation & Setup](#3-installation--setup)**
  * [3.1. MQTT Broker](#31-mqtt-broker)
  * [3.2. MongoDB](#32-mongodb)
* **[4. Usage](#4-usage)**
* **[5. Contributing](#5-contributing)**


## 1. Introduction
Short description of the project.

## 2. Pre-requirements
### 2.1. Ubuntu
* **Update** the package list:
    ```bash
    sudo apt update
    ```
  
* Install Git from your terminal:
    ```bash
    sudo apt install git
    ```

* Install Oracle's **Java SDK** (at least Java 8):
  Download JDK 11 from Oracle's official site, then install it.  
  ```bash
    sudo dpkg -i jdk-<expected java version>.deb
    ```

* Install **Maven** with the following command:
    ```bash
    sudo apt install maven
    ```
  
* Download the Eclipse BaSyx SDK with the following command: 
    ```bash
    git clone https://github.com/eclipse-basyx/basyx-java-sdk.git
    ```
  Install the SDK with Maven by navigating into the downloaded repository's directory and run the following command from your terminal:
    ```bash
    mvn clean install -U -DskipTests
    ```

* Clone this Repository and run it in any IDE you prefer, for example IntellIJ.
    ```bash
    git clone <Url of this repository>
    ```
  
## 3. Installation & Setup
### 3.1. MQTT Broker
For the MQTT Communication we need an MQTT Broker, so first of all we have to install `mosquitto` from our terminal with the following command:
  ```bash
  sudo apt install mosquitto
  ```

* We are able to check status, enable/disable, start, restart and stop our mosquitto broker with the proper commands of the following:
  ```bash
  sudo systemctl status mosquitto
  sudo systemctl enable mosquitto
  sudo systemctl disable mosquitto
  sudo systemctl start mosquitto
  sudo systemctl restart mosquitto
  sudo systemctl stop mosquitto
  ```

* If the `1883` port is already in use, we can make it free easily with these commands (identify Process ID of port `1883`, if it is used by any other process, we "kill" it), then we are able to start our broker without any issue:
  ```bash
  sudo lsof -i :1883
  sudo kill -9 <PID>
  ```
  
### 3.2. MongoDB
For our AAS Server we have to install MongoDB to be able to create databases and to be able to check them through an application with GUI (MongoDB Compass).
As our first step we have to download and install our MongoDB Server (Community Edition) and the MongoDB Compass from the official sites of MongoDB.

* MongoDB Server: https://www.mongodb.com/try/download/community
* MongoDB Compass: https://www.mongodb.com/try/download/shell

After download, we have to install MongoDB Server and MongoDB Compass with these commands:
  ```bash
  sudo dpkg -i mongodb-org-server_<verion number>_amd64.deb
  sudo dpkg -i mongodb-compass_<version number>_amd64.deb
  ```

As our last step we can check status, enable/disable, start, restart and stop our MongoDB Server:
  ```bash
  sudo systemctl status mongod
  sudo systemctl enable mongod
  sudo systemctl disable mongod
  sudo systemctl start mongod
  sudo systemctl restart mongod
  sudo systemctl stop mongod
  ```


## 4. Usage

### MQTT Commands:
  ```bash
  mosquitto_sub -h localhost -t power/consumed -v
  sudo tail -f /var/log/mosquitto/mosquitto.log
  ```

### IoT Device Simulator
  ```bash
  sh runsimulator.sh
  ```

## 5. Contributions
