import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:iot/main.dart';
import 'package:iot/model/device.dart';
import 'package:iot/model/user.dart';
import 'package:iot/view/company/company-users-page.dart';
import 'package:iot/view/sensor-card.dart';

class CompanyDevicesPage extends StatefulWidget {
  User user;

  CompanyDevicesPage({super.key, required this.user});

  @override
  State<CompanyDevicesPage> createState() => _CompanyDevicesPageState();
}

class _CompanyDevicesPageState extends State<CompanyDevicesPage> {
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
  }

  navigateToUsers() {
    Navigator.push(
      context,
      CupertinoPageRoute(
        builder: (context) => CompanyUsersPage(
          user: widget.user,
        ),
      ),
    );
  }

  navigateToDevices() {
    Navigator.push(
      context,
      CupertinoPageRoute(
        builder: (context) => CompanyDevicesPage(
          user: widget.user,
        ),
      ),
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
                    ListTile(
                      title: const Text("Users"),
                      onTap: navigateToUsers,
                    ),
                    ListTile(
                      title: const Text("Devices"),
                      onTap: navigateToDevices,
                    ),
                    const SizedBox(
                      height: 10,
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
        title: const Text("Devices"),
        actions: [
          IconButton(
            onPressed: () => print(""),
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
