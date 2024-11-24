import 'dart:async';
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:iot/common/constants.dart';
import 'package:iot/main.dart';
import 'package:iot/model/device.dart';
import 'package:iot/model/user.dart';
import 'package:iot/view/user/sensor-card.dart';

import 'sensor-card.dart';

class LandingPage extends StatefulWidget {
  User user;

  LandingPage({super.key, required this.user});

  @override
  State<LandingPage> createState() => _LandingPageState();
}

class _LandingPageState extends State<LandingPage> {
  final deviceNameController = TextEditingController();

  /*final descriptionController = TextEditingController();
  final companyController = TextEditingController();
  final data1Controller = TextEditingController();
  final data2Controller = TextEditingController();
  final data3Controller = TextEditingController();*/


  List devices = [
   // Device("1", 'Name 1'),
   // Device("2", 'Name 2'),
   // Device("3", 'Name 3'),
  ];
  bool isLoading = false;

  Future<void> loadData() async {
    setState(() {
      isLoading = true;
    });
    Timer(const Duration(seconds: 2), () {
      setState(() {
        isLoading = false;
      });
    });

    // // devices.clear();
    // await fetchTasksByUser();
    // await fetchTaskPrescriberUserData();
    // await fetchTaskPrescriberAvatar();
    // setState(() {
    //   isLoading = false;
    // });

    devices.clear();
    await getAllDevices(widget.user);
    setState(() {
       isLoading = false;
    });
  }

  getAllDevices(user) async{
      final registerRequest = {
        'name': user.username,
        'password': user.password,
      };
      try{
          String username = registerRequest['name'];
          final response = await http.get(
              Uri.parse('${Constants.BASE_URL}/person/devices?username=$username'),
              headers: {'Authorization': 'Bearer ${""}'},
          );
          if (response.statusCode == 200) {  
              List<dynamic> deviceList = jsonDecode(response.body);  
              setState(() {
                  devices = deviceList.map((deviceData) {
                  return Device.fromJson(deviceData); 
                  }).toList();
              });
          } else {
             print('Failed to load devices: ${response.statusCode}');
          }

      } catch(e){
            print('Error during device query: $e');
      }

  }

  
  addNewDevice(user) async {
  var deviceName = deviceNameController.text; 
  print('Device name: $deviceName');

  var createDeviceRequest = {
    'login': {
      'name': user.username, 
      'password': user.password 
    },
    'type': deviceName 
  };

  try {

    final response = await http.post(
      Uri.parse('${Constants.BASE_URL}/person/create_device'), 
      headers: {
        'Content-Type': 'application/json', 
      },
      body: jsonEncode(createDeviceRequest), 
    );


    if (response.statusCode == 201) {
      print("Device created successfully!");
      // TODO: loadData() !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    } else if (response.statusCode == 401) {
      print("Error: Login unsuccessful.");
    } else {
      print("Failed to create device. Status code: ${response.statusCode}");
      print("Error: ${response.body}");
    }
  } catch (e) {

    print("Error occurred while creating device: $e");
  }
}


 /* fetchTaskPrescriberUserData() async {
    for (Device device in devices) {
      final response = await http.get(
        Uri.parse(
          '${Constants.BASE_URL}/users/user/99999999999',
        ),
        headers: {'Authorization': 'Bearer ${""}'},
      );
      var decodedUser = jsonDecode(response.body);

      // task.creatorFirstName = decodedUser['firstName'];
      // task.creatorLastName = decodedUser['lastName'];
    }
  }*/

  addDevicePopup() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Add a new device'),
          content: SizedBox(
            height: 150,
            width: 300,
            child: Column(
              children: <Widget>[
                TextField(
                  decoration: const InputDecoration(
                    labelText: 'Device type',
                  ),
                  controller: deviceNameController,
                ),
              ],
            ),
          ),
          actions: <Widget>[
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: const Text('Cancel'),
            ),
            TextButton(
              onPressed: () {
                addNewDevice(widget.user);
                Navigator.of(context).pop();
              },
              child: const Text('Add'),
            ),
          ],
        );
      },
    );
  }

/*  addNewDevice() {
    var deviceName = deviceNameController.text;
    //TODO add device POST
    print(deviceName);
    
  }*/

  logout() {
    Navigator.push(
      context,
      CupertinoPageRoute(
        builder: (context) => WillPopScope(
          onWillPop: () async {
            // Return false to prevent navigation via the back button
            return false;
          },
          child: const LoginPage(title: ''),
        ),
      ),
    );
    widget.user = User("", "", "", 0);
    showDialog<String>(
      context: context,
      builder: (BuildContext context) => AlertDialog(
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(20),
          side: const BorderSide(color: Color.fromARGB(255, 255, 255, 255)),
        ),
        backgroundColor: Colors.white,
        elevation: 5,
        shadowColor: Colors.green,
        title: const Text(
          "Logged out",
          style: TextStyle(fontSize: 24, color: Colors.black),
        ),
        actions: [
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(
                onPressed: () => Navigator.pop(context, 'ok'),
                style: ButtonStyle(
                  fixedSize: WidgetStateProperty.all(const Size(100, 10)),
                  backgroundColor: const WidgetStatePropertyAll(Colors.white),
                  padding: WidgetStateProperty.all(
                    const EdgeInsets.only(left: 20, right: 20),
                  ),
                  shape: WidgetStateProperty.all<RoundedRectangleBorder>(
                    RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(50.0),
                      side: const BorderSide(
                        color: Color.fromRGBO(255, 255, 255, 1),
                        width: 1.4,
                      ),
                    ),
                  ),
                ),
                child: const Text("ok"),
              ),
            ],
          )
        ],
      ),
    );
  }

  deleteDevice(String id) async {
    final response = await http.delete(
      Uri.parse('${Constants.BASE_URL}/person/delete_device?device_id=$id'),
    );
  }

  @override
  void initState() {
    loadData();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      drawer: Drawer(
        backgroundColor: Colors.white,
        elevation: 200,
        child: Container(
          padding: const EdgeInsets.only(right: 1.3),
          child: Container(
            color: Colors.white,
            child: ListView(
              children: [
                DrawerHeader(
                  decoration: const BoxDecoration(
                    color: Colors.white,
                  ),
                  child: Text(widget.user.username,
                      style: const TextStyle(fontSize: 24)),
                ),
                Column(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const SizedBox(
                      height: 100,
                    ),
                    SizedBox(
                      width: 200,
                      child: Column(
                        children: [
                          const Text(
                            "Logout",
                            style: TextStyle(fontWeight: FontWeight.bold),
                          ),
                          ElevatedButton(
                            onPressed: logout,
                            style: ButtonStyle(
                              fixedSize:
                                  const WidgetStatePropertyAll(Size(150, 10)),
                              backgroundColor: WidgetStateProperty.all(
                                  const Color.fromRGBO(203, 38, 38, 1)),
                              shape: WidgetStateProperty.all<
                                  RoundedRectangleBorder>(
                                RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(50.0),
                                ),
                              ),
                            ),
                            child: const Icon(
                              Icons.logout,
                              color: Color.fromRGBO(0, 0, 0, 1),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
      appBar: AppBar(
        backgroundColor: Colors.white,
        title: const Text("Your devices"),
        actions: [
          IconButton(
            onPressed: addDevicePopup,
            icon: const Icon(Icons.add),
            color: Colors.black,
          ),
        ],
      ),
      body: Stack(children: [
        RefreshIndicator(
          onRefresh: loadData,
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Expanded(
                  child: ListView.builder(
                    itemCount: devices.length,
                    itemBuilder: (BuildContext context, index) {
                      return SensorCard(
                        device: devices[index],
                        user: widget.user,
                        deleteDeviceCallback: deleteDevice,
                      );
                    },
                  ),
                )
              ],
            ),
          ),
        ),
        if (isLoading)
          const Center(
            child: CircularProgressIndicator(),
          ),
      ]),
    );
  }
}
