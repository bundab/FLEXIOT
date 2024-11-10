# IotRegistrationSystemRepository

## Table of Contents
* **[1. Introduction](#1-introduction)**
* **[2. Pre-requirements](#2-pre-requirements)**
  * [2.1. Ubuntu](#21-ubuntu)
* **[3. Installation & Setup](#3-installation--setup)**
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

## 4. Usage

## 5. Contributions
