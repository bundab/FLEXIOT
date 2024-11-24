class Company {
  Company(this.username, this.password);
  String username;
  String password;

    factory Company.fromJson(Map<String, dynamic> json) {
        return Company(
        json['name'],
        json['password']
        );
    }

Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['username'] = this.username;
    data['password'] = this.password;
    return data;
    }
}
