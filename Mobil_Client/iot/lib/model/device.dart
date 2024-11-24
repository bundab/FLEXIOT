class Device {
  Device(this.id, this.type);
  String id;
  String type;

  Device.fromJson(Map<String, dynamic> json)
      : id = json['id'],
        type = json['type'];

  Map<String, dynamic> toJson() => {'id': id, 'type': type};
}
