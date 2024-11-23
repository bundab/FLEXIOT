class Device {
  Device(
      this.id, this.name, this.description, this.currentValue, this.addedDate);
  int id;
  String name;
  String description;
  String currentValue;
  DateTime addedDate;

  Device.fromJson(Map<String, dynamic> json)
      : id = json['id'],
        name = json['name'],
        description = json['description'],
        currentValue = json['currentValue'],
        addedDate = DateTime.parse((json['addedDate']));
}
