import 'dart:async';
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:iot/common/constants.dart';
import 'package:iot/main.dart';
import 'package:iot/model/device.dart';
import 'package:iot/model/user.dart';
import 'package:iot/view/sensor-card.dart';

class LandingPage extends StatefulWidget {
  User user;

  LandingPage({super.key, required this.user});

  @override
  State<LandingPage> createState() => _LandingPageState();
}

class _LandingPageState extends State<LandingPage> {
  final deviceNameController = TextEditingController();

  final descriptionController = TextEditingController();
  final companyController = TextEditingController();
  final data1Controller = TextEditingController();
  final data2Controller = TextEditingController();
  final data3Controller = TextEditingController();

  List devices = [
    Device(0, 'Name 1', 'Description', 'Current value', DateTime.now()),
    Device(1, 'Name 2', 'Description', 'Current value', DateTime.now()),
    Device(2, 'Name 3', 'Description', 'Current value', DateTime.now()),
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
  }

  fetchTasksByUser() async {
    final response = await http.get(
      Uri.parse(
        '${Constants.BASE_URL}/task/allByUserId/${widget.user.id}',
      ),
      headers: {'Authorization': 'Bearer ${widget.user.token}'},
    );
    final decodedList = json.decode(response.body);
    for (var element in decodedList) {
      devices.add(Device.fromJson(element));
    }
  }

  fetchTaskPrescriberUserData() async {
    for (Device device in devices) {
      final response = await http.get(
        Uri.parse(
          '${Constants.BASE_URL}/users/user/99999999999',
        ),
        headers: {'Authorization': 'Bearer ${widget.user.token}'},
      );
      var decodedUser = jsonDecode(response.body);

      // task.creatorFirstName = decodedUser['firstName'];
      // task.creatorLastName = decodedUser['lastName'];
    }
  }

  fetchTaskPrescriberAvatar() async {
    for (Device task in devices) {
      try {
        final response = await http.get(
          Uri.parse('${Constants.BASE_URL}/users/images/99999999.png'),
          headers: {'Authorization': 'Bearer ${widget.user.token}'},
        );

        if (response.statusCode == 200) {
          setState(() {
            // task.creatorImage = response.bodyBytes;
          });
        } else {}
      } catch (e) {
        print(e);
      }
    }
  }

  addDevicePopup() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Add a new device'),
          content: SizedBox(
            height: 400,
            width: 300,
            child: Column(
              children: <Widget>[
                TextField(
                  decoration: const InputDecoration(
                    labelText: 'Device name',
                  ),
                  controller: deviceNameController,
                ),
                TextField(
                  decoration: const InputDecoration(
                    labelText: 'Description',
                  ),
                  controller: descriptionController,
                ),
                TextField(
                  decoration: const InputDecoration(
                    labelText: 'Company',
                  ),
                  controller: companyController,
                ),
                TextField(
                  decoration: const InputDecoration(
                    labelText: 'Data1',
                  ),
                  controller: data1Controller,
                ),
                TextField(
                  decoration: const InputDecoration(
                    labelText: 'Data2',
                  ),
                  controller: data2Controller,
                ),
                TextField(
                  decoration: const InputDecoration(
                    labelText: 'Data3',
                  ),
                  controller: data3Controller,
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
                addNewDevice();
                Navigator.of(context).pop();
              },
              child: const Text('Add'),
            ),
          ],
        );
      },
    );
  }

  addNewDevice() {
    var deviceName = deviceNameController.text;
    var description = descriptionController.text;
    var company = companyController.text;
    var data1 = data1Controller.text;
    var data2 = data2Controller.text;
    var data3 = data3Controller.text;

    print(deviceName);
    print(description);
    print(company);
    print(data1);
    print(data2);
    print(data3);
  }

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
    widget.user = User(0, '', '', '', '', false);
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
