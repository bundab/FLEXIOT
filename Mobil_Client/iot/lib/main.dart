import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:iot/common/constants.dart';
import 'package:iot/model/user.dart';
import 'package:http/http.dart' as http;
import 'package:iot/view/company/company-users-page.dart';
import 'package:iot/view/user/landing-page.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      home: LoginPage(title: 'Login'),
    );
  }
}

class LoginPage extends StatefulWidget {
  const LoginPage({super.key, required this.title});
  final String title;

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final loginUsernameTextController = TextEditingController();
  final loginPasswordTextController = TextEditingController();
  final registerUsernameTextController = TextEditingController();
  final registerPasswordTextController = TextEditingController();

  User user = User(0, '', '', '', '', true);

  login() async {
    String username = loginUsernameTextController.text;
    String password = loginPasswordTextController.text;

    // final authenticationResponse = await authenticate(username, password);

    // if (authenticationResponse.statusCode != 200) {
    // print(authenticationResponse.statusCode);
    // return;
    // }

    // final token = await json.decode(authenticationResponse.body)['token'];

    // final userDataResponse = await fetchUserData(username, token);
    // final decodedUserData = json.decode(userDataResponse.body);

    // user = User(
    //   decodedUserData['id'],
    //   decodedUserData['firstName'],
    //   decodedUserData['lastName'],
    //   decodedUserData['username'],
    //   token,
    //   false,
    // );

    navigateAfterSubmit();
  }

  register() async {
    String username = registerUsernameTextController.text;
    String password = registerPasswordTextController.text;

// final authenticationResponse = await authenticate(username, password);

    // if (authenticationResponse.statusCode != 200) {
    // print(authenticationResponse.statusCode);
    // return;
    // }

    // final token = await json.decode(authenticationResponse.body)['token'];

    // final userDataResponse = await fetchUserData(username, token);
    // final decodedUseaarData = json.decode(userDataResponse.body);

    // user = User(
    //   decodedUserData['id'],
    //   decodedUserData['firstName'],
    //   decodedUserData['lastName'],
    //   decodedUserData['username'],
    //   token,
    //   false,
    // );

    navigateAfterSubmit();
  }

  httpLogin(String username, String password) async {
    return await http.post(
      Uri.parse('${Constants.BASE_URL}/auth/login'),
      headers: {
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        <String, String>{'username': username, 'password': password},
      ),
    );
  }

  httpRegister(String username, String password) async {
    return await http.post(
      Uri.parse('${Constants.BASE_URL}/auth/register'),
      headers: {
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(
        <String, String>{'username': username, 'password': password},
      ),
    );
  }

  navigateAfterSubmit() {
    if (!user.isAdmin) {
      Navigator.push(
        context,
        CupertinoPageRoute(
          builder: (context) => LandingPage(
            user: user,
          ),
        ),
      );
    } else {
      Navigator.push(
        context,
        CupertinoPageRoute(
          builder: (context) => CompanyUsersPage(
            user: user,
          ),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: Colors.white,
        automaticallyImplyLeading: false,
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          const Text(
            "Welcome back!",
            style: TextStyle(fontSize: 40, fontWeight: FontWeight.bold),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 30),
          Container(
            padding: const EdgeInsets.only(top: 10, left: 30, right: 30),
            decoration: BoxDecoration(
                color: Colors.white, borderRadius: BorderRadius.circular(20)),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const Text(
                  "Sign in",
                  style: TextStyle(
                      color: Colors.black,
                      fontSize: 30,
                      fontWeight: FontWeight.bold),
                ),
                const SizedBox(height: 10),
                TextField(
                  controller: loginUsernameTextController,
                  decoration: const InputDecoration(
                    contentPadding: EdgeInsets.symmetric(horizontal: 20.0),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(20.0)),
                    ),
                    hintText: "username",
                    hintStyle: TextStyle(color: Colors.grey),
                  ),
                  style: const TextStyle(
                      color: Colors.black, fontWeight: FontWeight.bold),
                  onChanged: (event) => setState(() {}),
                  onTapOutside: (event) => setState(() {}),
                ),
                const SizedBox(height: 10),
                TextField(
                  onTapOutside: (event) => setState(() {}),
                  onChanged: (event) => setState(() {}),
                  controller: loginPasswordTextController,
                  obscureText: true,
                  enableSuggestions: false,
                  autocorrect: false,
                  decoration: const InputDecoration(
                    contentPadding: EdgeInsets.symmetric(horizontal: 20.0),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(20.0)),
                    ),
                    hintText: "password",
                    hintStyle: TextStyle(color: Colors.grey),
                  ),
                  style: const TextStyle(
                    color: Colors.black,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const SizedBox(
                  height: 20,
                ),
                AnimatedContainer(
                  duration: const Duration(milliseconds: 500),
                  decoration: BoxDecoration(
                    color: (loginUsernameTextController.text.isEmpty ||
                            loginPasswordTextController.text.isEmpty)
                        ? Colors.grey
                        : Colors.blue,
                    borderRadius: BorderRadius.circular(30),
                  ),
                  child: ElevatedButton(
                    onPressed: (loginUsernameTextController.text.isEmpty ||
                            loginPasswordTextController.text.isEmpty)
                        ? null
                        : login,
                    style: ButtonStyle(
                      padding: WidgetStateProperty.all(
                        const EdgeInsets.only(left: 50, right: 50),
                      ),
                      backgroundColor: WidgetStateProperty.all(
                        Colors.transparent,
                      ),
                      elevation: WidgetStateProperty.all(0),
                    ),
                    child: const Icon(
                      Icons.login,
                      color: Colors.white,
                    ),
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(
            height: 50,
          ),
          Container(
            padding: const EdgeInsets.only(top: 10, left: 30, right: 30),
            decoration: BoxDecoration(
                color: Colors.white, borderRadius: BorderRadius.circular(20)),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const Text(
                  "Register",
                  style: TextStyle(
                      color: Colors.black,
                      fontSize: 30,
                      fontWeight: FontWeight.bold),
                ),
                const SizedBox(height: 10),
                TextField(
                  controller: registerUsernameTextController,
                  decoration: const InputDecoration(
                    contentPadding: EdgeInsets.symmetric(horizontal: 20.0),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(20.0)),
                    ),
                    hintText: "username",
                    hintStyle: TextStyle(color: Colors.grey),
                  ),
                  style: const TextStyle(
                      color: Colors.black, fontWeight: FontWeight.bold),
                  onChanged: (event) => setState(() {}),
                  onTapOutside: (event) => setState(() {}),
                ),
                const SizedBox(height: 10),
                TextField(
                  onTapOutside: (event) => setState(() {}),
                  onChanged: (event) => setState(() {}),
                  controller: registerPasswordTextController,
                  obscureText: true,
                  enableSuggestions: false,
                  autocorrect: false,
                  decoration: const InputDecoration(
                    contentPadding: EdgeInsets.symmetric(horizontal: 20.0),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(20.0)),
                    ),
                    hintText: "password",
                    hintStyle: TextStyle(color: Colors.grey),
                  ),
                  style: const TextStyle(
                    color: Colors.black,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const SizedBox(
                  height: 20,
                ),
                AnimatedContainer(
                  duration: const Duration(milliseconds: 500),
                  decoration: BoxDecoration(
                    color: (registerUsernameTextController.text.isEmpty ||
                            registerPasswordTextController.text.isEmpty)
                        ? Colors.grey
                        : Colors.blue,
                    borderRadius: BorderRadius.circular(30),
                  ),
                  child: ElevatedButton(
                    onPressed: (registerUsernameTextController.text.isEmpty ||
                            registerPasswordTextController.text.isEmpty)
                        ? null
                        : login,
                    style: ButtonStyle(
                      padding: WidgetStateProperty.all(
                        const EdgeInsets.only(left: 50, right: 50),
                      ),
                      backgroundColor: WidgetStateProperty.all(
                        Colors.transparent,
                      ),
                      elevation: WidgetStateProperty.all(0),
                    ),
                    child: const Icon(
                      Icons.login,
                      color: Colors.white,
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
