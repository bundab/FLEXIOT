import 'package:flutter/material.dart';
import 'package:iot/model/company.dart';
import 'package:iot/model/device.dart';
import 'package:iot/model/user.dart';
import 'package:iot/view/company/company-sensor-detail.dart';

class CompanySensorCard extends StatefulWidget {
  CompanySensorCard({
    super.key,
    required this.company,
    required this.device,
    required this.deleteDeviceCallback
  });
  Company company;
  Device device;
  final Function deleteDeviceCallback;

  @override
  State<CompanySensorCard> createState() => _CompanySensorCardState();
}

class _CompanySensorCardState extends State<CompanySensorCard> {
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => CompanySensorDetail(
            device: widget.device,
            company: widget.company,
          ),
        ),
      ),
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 500),
        padding: const EdgeInsets.all(10),
        margin: const EdgeInsets.all(10),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(20),
          border: Border.all(
            color: const Color.fromARGB(255, 191, 191, 191),
            width: 1,
          ),
          color: Colors.white70,
        ),
        child: Column(
          children: [
            Container(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        widget.device.id,
                        style: const TextStyle(
                            color: Colors.black,
                            fontSize: 25,
                            fontWeight: FontWeight.bold),
                      ),
                      IconButton(
                        onPressed: () => widget.deleteDeviceCallback(widget.device.id),
                        icon: const Icon(
                          Icons.delete,
                          color: Colors.red,
                        ),
                      ),
                    ],
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      Row(
                        children: [
                          Column(
                            children: [
                              Row(
                                mainAxisAlignment:
                                    MainAxisAlignment.spaceBetween,
                                children: [
                                  const Text("Id:"),
                                  Text(widget.device.id),
                                ],
                              ),
                              Row(
                                mainAxisAlignment:
                                    MainAxisAlignment.spaceBetween,
                                children: [
                                  const Text("Type:"),
                                  Text(widget.device.type),
                                ],
                              ),
                              
                            ],
                          ),
                        ],
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
