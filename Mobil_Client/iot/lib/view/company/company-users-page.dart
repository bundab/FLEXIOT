import 'dart:async';
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:iot/main.dart';
import 'package:iot/model/company.dart';
import 'package:iot/model/device.dart';
import 'package:iot/model/user.dart';
import 'package:iot/view/company/company-devices-page.dart';
import 'package:iot/view/company/company-user-card.dart';
import 'package:iot/common/constants.dart';
import 'package:http/http.dart' as http;

class CompanyUsersPage extends StatefulWidget {
  Company company;

  CompanyUsersPage({super.key, required this.company});

  @override
  State<CompanyUsersPage> createState() => _CompanyUsersPageState();
}

class _CompanyUsersPageState extends State<CompanyUsersPage> {
  final userNameController = TextEditingController();

  /*final descriptionController = TextEditingController();
  final companyController = TextEditingController();
  final data1Controller = TextEditingController();
  final data2Controller = TextEditingController();
  final data3Controller = TextEditingController();*/

  List<dynamic> usersList = [];
  List users = [
    //User("1", 'Name 1', "", 0),
    //User("2", 'Name 2', "", 0),
    //User("3", 'Name 3', "", 0),
  ];
  bool isLoading = false;

  Future<void> loadData() async {
    setState(() {
      isLoading = true;
    });

    
    await getAllUsers(widget.company);
    
    Timer(const Duration(seconds: 2), () {
      setState(() {
        isLoading = false;
      });
    });
  }

  getAllUsers(company) async {
  try {
    print("user data:");
    print(company.username);
    print(company.password);

    final response = await http.get(
      Uri.parse('${Constants.BASE_URL}/company/users?name=${company.username}&password=${company.password}'), 
    );

    if (response.statusCode == 200) {
      usersList = jsonDecode(response.body); 
      users = usersList.map((user) => User.fromJson(user)).toList();
    } else {
      print('Failed to load users: ${response.statusCode}'); 
    }
  } catch (e) {
    print('Error during users query: $e'); 
  }
}

  navigateToUsers() {
    Navigator.push(
      context,
      CupertinoPageRoute(
        builder: (context) => CompanyUsersPage(
          company: widget.company,
        ),
      ),
    );
  }

  navigateToDevices() {
    Navigator.push(
      context,
      CupertinoPageRoute(
        builder: (context) => CompanyDevicesPage(
          company: widget.company,
        ),
      ),
    );
  }

  addNewUser(company) async {
      var userName = userNameController.text;
       print(userName);
        var loginRequest = {
          'name': company.username, 
          'password': company.password
        };

      try {
        final response = await http.post(
          Uri.parse('${Constants.BASE_URL}/company/addPerson/$userName'), 
          headers: {
            'Content-Type': 'application/json',
          },
          body: jsonEncode(loginRequest), 
        );

        if (response.statusCode == 200) {
          print("Person added to company successfully!");
        } else if (response.statusCode == 404) {
          print("Error: ${jsonDecode(response.body)}");
        } else if (response.statusCode == 401) {
          print("Error: Invalid password for company.");
        } else if (response.statusCode == 409) {
          print("Error: Person is already in this company.");
        } else {
          print("Failed to add person to company. Status code: ${response.statusCode}");
        }
      } catch (e) {
        print("Error occurred while adding user: $e");
      }

      setState(() {
        users.clear();
        loadData();
      });     

  }


  logout() {
    Navigator.push(
      context,
      CupertinoPageRoute(
        builder: (context) => WillPopScope(
          onWillPop: () async {
            return false;
          },
          child: const LoginPage(title: ''),
        ),
      ),
    );
    widget.company = Company('', '');
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

  showAddUserDialog() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Add user'),
          content: SizedBox(
            height: 60,
            width: 300,
            child: Column(
              children: <Widget>[
                TextField(
                  decoration: const InputDecoration(
                    labelText: 'Username',
                  ),
                  controller: userNameController,
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
                addNewUser(widget.company);
                Navigator.of(context).pop();
              },
              child: const Text('Add'),
            ),
          ],
        );
      },
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
                  child: Text(widget.company.username,
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
        title: const Text("Users"),
        actions: [
          IconButton(
            onPressed: () => showAddUserDialog(),
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
                    itemCount: users.length,
                    itemBuilder: (BuildContext context, index) {
                      return UserCard(
                        user: users[index],
                        company: widget.company,
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
