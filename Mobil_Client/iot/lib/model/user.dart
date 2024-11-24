import 'package:iot/model/company.dart';
import 'package:iot/model/device.dart';

class User {
  User(this.id, this.username, this.password, this.age);
  String id;
  String username;
  String password;
  num age;

 // List<Device> devices;
  //Company? company;

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      json['id'],
      json['username'],
      json['password'],
      json['age']
      //(json['devices'] as List).map((device) => Device.fromJson(device)).toList(),
      //json['company'] != null ? Company.fromJson(json['company']) : null,
    );
  } 

Map<String, dynamic> toJson() => {
        'id': id,
        'username': username,
        'password': password
        // 'age': age,
        // 'devices': '',
        // 'company': '',
      };
}